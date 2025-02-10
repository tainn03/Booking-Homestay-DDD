package nnt.com.controller.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.application.service.payment.PaymentAppService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class PaymentController {
    PaymentAppService paymentAppService;

    @GetMapping("/order")
    public void submitOrder(@RequestParam("amount") int amount,
                            @RequestParam("orderInfo") String orderInfo,
                            HttpServletRequest request, HttpServletResponse response) throws IOException {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        paymentAppService.redirectToPaymentGateway(amount, orderInfo, baseUrl, response);
    }

    // Sau khi hoàn tất thanh toán, VNPAY sẽ chuyển hướng trình duyệt về URL này
    @GetMapping("/vnpay-payment")
    public void handleVNPayPayment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        paymentAppService.handlePaymentResponse(request, response);
    }
}
