package nnt.com.domain.aggregates.service;

import jakarta.servlet.http.HttpServletRequest;
import nnt.com.domain.aggregates.model.entity.Payment;

public interface PaymentService {
    String createOrder(int total, String orderInfor, String urlReturn);

    int orderReturn(HttpServletRequest request);

    Payment save(Payment payment);

    Payment update(Payment payment);

    Payment findById(Long id);

    void delete(Long id);
}
