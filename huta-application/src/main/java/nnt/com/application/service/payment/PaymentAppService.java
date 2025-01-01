package nnt.com.application.service.payment;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface PaymentAppService {
    void redirectToPaymentGateway(int amount, String orderInfor, String baseUrl, HttpServletResponse response) throws IOException;

    void handlePaymentResponse(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
