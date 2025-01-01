package nnt.com.infrastructure.persistence.payment.repository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.base.exception.BusinessException;
import nnt.com.domain.base.exception.ErrorCode;
import nnt.com.domain.payment.model.entity.Payment;
import nnt.com.domain.payment.repository.PaymentDomainRepository;
import nnt.com.infrastructure.persistence.payment.database.jpa.PaymentInfraRepositoryJpa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class PaymentInfraRepositoryImpl implements PaymentDomainRepository {
    PaymentInfraRepositoryJpa paymentInfraRepositoryJpa;

    @Override
    public Payment save(Payment payment) {
        return paymentInfraRepositoryJpa.save(payment);
    }

    @Override
    public Payment update(Payment payment) {
        return paymentInfraRepositoryJpa.save(payment);
    }

    @Override
    public Payment getById(Long id) {
        return paymentInfraRepositoryJpa.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.PAYMENT_NOT_FOUND));
    }

    @Override
    public Page<Payment> getAll(int page, int size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        return paymentInfraRepositoryJpa.findAll(pageable);
    }

    @Override
    public void delete(Long id) {
        paymentInfraRepositoryJpa.deleteById(id);
    }
}
