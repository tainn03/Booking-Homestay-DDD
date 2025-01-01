package nnt.com.application.service.payment.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.application.service.payment.PaymentAppService;
import nnt.com.domain.payment.service.VNPayService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class PaymentAppServiceImpl implements PaymentAppService {
    VNPayService vnPayService;

    @Override
    public void redirectToPaymentGateway(int amount, String orderInfor, String baseUrl, HttpServletResponse response) throws IOException {
        String vnpayUrl = vnPayService.createOrder(amount, orderInfor, baseUrl);
        response.sendRedirect(vnpayUrl);
    }

    @Override
    public void handlePaymentResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int paymentStatus = vnPayService.orderReturn(request);
        if (paymentStatus == 0) {
            log.error("PAYMENT FAIL");
            redirectToReactClient(response, null);
            return;
        }
        String orderInfo = request.getParameter("vnp_OrderInfo");

        // Xử lý thông tin đơn hàng
        handleRequestParams(request, paymentStatus, orderInfo);
        redirectToReactClient(response, orderInfo);
    }

    private void redirectToReactClient(HttpServletResponse response, String orderInfo) throws IOException {
        if (orderInfo != null) {
            String redirectUrl = "http://localhost:3000/bookingdetail?id=" + orderInfo;
            response.sendRedirect(redirectUrl);
        } else {
            response.sendRedirect("http://localhost:3000/paymentfail");
        }
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
    }
}
