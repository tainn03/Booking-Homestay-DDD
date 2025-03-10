package nnt.com.application.service.payment.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.application.service.payment.PaymentAppService;
import nnt.com.domain.aggregates.model.entity.Payment;
import nnt.com.domain.aggregates.model.entity.UserSubscription;
import nnt.com.domain.aggregates.model.vo.PaymentMethod;
import nnt.com.domain.aggregates.model.vo.PaymentStatus;
import nnt.com.domain.aggregates.repository.PaymentDomainRepository;
import nnt.com.domain.aggregates.repository.UserSubscriptionDomainRepository;
import nnt.com.domain.aggregates.service.PaymentService;
import nnt.com.infrastructure.distributed.kafka.producer.KafkaProducer;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class PaymentAppServiceImpl implements PaymentAppService {
    PaymentService vnPayService;
    UserSubscriptionDomainRepository userSubscriptionDomainRepository;
    PaymentDomainRepository paymentDomainRepository;
    KafkaProducer kafkaProducer;

    @Override
    public void redirectToPaymentGateway(int amount, String orderInfor, String baseUrl, HttpServletResponse response) throws IOException {
        String vnpayUrl = vnPayService.createOrder(amount, orderInfor, baseUrl);
        log.info("URL TO VNPAY: {}", vnpayUrl);
        response.sendRedirect(vnpayUrl);
    }

    @Override
    @Transactional
    public void handlePaymentResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int paymentStatus = vnPayService.orderReturn(request);
        if (paymentStatus == 0) {
            log.error("PAYMENT FAIL");
            redirectToReactClient(response, null);
            return;
        }
        String orderInfo = request.getParameter("vnp_OrderInfo");

        // Xử lý thông tin đơn hàng
        if (orderInfo.split(":")[0].equals("BOOKING")) {
            handleRequestParams(request, paymentStatus, orderInfo);
        } else {
            handleSubscriptionRequestParams(request, paymentStatus, orderInfo.split(":")[1]);
        }
        redirectToReactClient(response, orderInfo);
    }

    private void handleSubscriptionRequestParams(HttpServletRequest request, int paymentStatus, String orderInfo) {
        String totalPrice = String.valueOf(Integer.parseInt(request.getParameter("vnp_Amount")) / 100);
        UserSubscription userSubscription = userSubscriptionDomainRepository.getById(Long.valueOf(orderInfo));
        long month = (Integer.parseInt(totalPrice) / userSubscription.getSubscription().getPrice());
        if (month > 0) {
            userSubscription.setExpiredAt(userSubscription.getExpiredAt().plusMonths(month));
            userSubscription.setStatus("ACTIVE");
        } else {
            userSubscription.setExpiredAt(userSubscription.getExpiredAt().plusMonths(1));
            userSubscription.setStatus("INACTIVE");
        }
        userSubscription.setExpiredAt(userSubscription.getExpiredAt().plusMonths(month > 0 ? month : 1));
        Payment payment = Payment.builder()
                .amount(Integer.parseInt(totalPrice))
                .userSubscription(userSubscription)
                .status(paymentStatus == 1 ? PaymentStatus.SUCCESS.name() : PaymentStatus.FAIL.name())
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .build();
        if (userSubscription.getPayments() != null) {
            userSubscription.getPayments().add(payment);
        } else {
            userSubscription.setPayments(List.of(payment));
        }
        userSubscriptionDomainRepository.save(userSubscription);
    }

    private void redirectToReactClient(HttpServletResponse response, String orderInfo) throws IOException {
        if (orderInfo != null) {
            String redirectUrl;
            if (orderInfo.split(":")[0].equals("BOOKING")) {
                redirectUrl = "http://localhost:3000/pay-done?id=" + orderInfo;
            } else {
                redirectUrl = "http://localhost:3000/account-package";
            }
            response.sendRedirect(redirectUrl);
        } else {
            response.sendRedirect("http://localhost:3000/paymentfail");
        }
    }

    private void sendRequestToKafka(String orderInfo) {
        // Gửi thông tin đơn hàng về Kafka
        log.info("SEND REQUEST TO KAFKA: {}", orderInfo);
    }

    private void handleRequestParams(HttpServletRequest request, int paymentStatus, String orderInfo) {
        LocalDateTime paymentTime =
                LocalDateTime.parse(request.getParameter("vnp_PayDate"),
                        DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = String.valueOf(Integer.parseInt(request.getParameter("vnp_Amount")) / 100);
        String vnp_BankCode = request.getParameter("vnp_BankCode");
        String vnp_BankTranNo = request.getParameter("vnp_BankTranNo");
        String vnp_CardType = request.getParameter("vnp_CardType");
        String vnp_TxnRef = request.getParameter("vnp_TxnRef");
        String vnp_SecureHash = request.getParameter("vnp_SecureHash");

        // Tạo thanh toán mới cho đơn đặt phòng
        log.info("Payment status: {}", paymentStatus);
        log.info("Order info: {}", orderInfo);
        log.info("Payment time: {}", paymentTime);
        log.info("Transaction ID: {}", transactionId);
        log.info("Total price: {}", totalPrice);
        log.info("Bank code: {}", vnp_BankCode);
        log.info("Bank transaction number: {}", vnp_BankTranNo);
        log.info("Card type: {}", vnp_CardType);
        log.info("Transaction reference: {}", vnp_TxnRef);
        log.info("Secure hash: {}", vnp_SecureHash);

        log.info("CREATE PAYMENT WITH TOTAL PRICE: {}", totalPrice);
        String bookingCode = orderInfo.split(":")[1];
        Payment payment = Payment.builder()
                .amount(Integer.parseInt(totalPrice))
                .transactionId(transactionId)
                .status(paymentStatus == 1 ? PaymentStatus.SUCCESS.name() : PaymentStatus.FAIL.name())
                .note("Thanh toán thành công với VNPay")
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .booking(null)
                .build();

        log.info("SEND PAYMENT MESSAGE TO KAFKA");
        kafkaProducer.sendFireAndForgot("payment.mail", bookingCode, payment);
    }
}
