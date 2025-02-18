package nnt.com.domain.aggregates.service;

import jakarta.servlet.http.HttpServletRequest;

public interface PaymentService {
    String createOrder(int total, String orderInfor, String urlReturn);

    int orderReturn(HttpServletRequest request);
}
