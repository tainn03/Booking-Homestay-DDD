package nnt.com.domain.aggregates.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.domain.aggregates.model.entity.Payment;
import nnt.com.domain.aggregates.repository.PaymentDomainRepository;
import nnt.com.domain.aggregates.service.PaymentDomainService;
import nnt.com.domain.shared.utils.VNPayUtil;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class PaymentServiceImpl implements PaymentDomainService {
    VNPayUtil vnpayUtil;
    PaymentDomainRepository paymentDomainRepository;

    @Override
    public String createOrder(int total, String orderInfor, String urlReturn) {
        Map<String, String> vnp_Params = vnpayUtil.createVnPayParams(total, orderInfor, urlReturn);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = vnpayUtil.hmacSHA512(hashData.toString());
        return vnpayUtil.createQueryUrl(queryUrl, vnp_SecureHash);
    }

    @Override
    public int orderReturn(HttpServletRequest request) {
        Map fields = new HashMap();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = null;
            String fieldValue = null;
            fieldName = URLEncoder.encode(params.nextElement(), StandardCharsets.US_ASCII);
            fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");
        String signValue = vnpayUtil.hashAllFields(fields);
        if (signValue.equals(vnp_SecureHash)) {  // 1: success, 0: fail, -1: error
            if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return -1;
        }
    }

    @Override
    public Payment save(Payment payment) {
        return paymentDomainRepository.save(payment);
    }

    @Override
    public Payment update(Payment payment) {
        return paymentDomainRepository.update(payment);
    }

    @Override
    public Payment findById(Long id) {
        return paymentDomainRepository.getById(id);
    }

    @Override
    public void delete(Long id) {
        paymentDomainRepository.delete(id);
    }

    @Override
    public long getTotalPaymentByBooking(long id) {
        try {
            log.info("GET TOTAL PAYMENT BY BOOKING ID {}", id);
            Payment payment = paymentDomainRepository.getByBookingId(id);
            log.info("FOUND PAYMENT FOR BOOKING ID {} WITH AMOUNT {}", id, payment.getAmount());
            return payment.getAmount();
        } catch
        (Exception e) {
            log.error("ERROR GETTING TOTAL PAYMENT BY BOOKING ID {}: {}", id, e.getMessage());
            return 0;
        }
    }
}
