package nnt.com.infrastructure.persistence.payment.repository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.base.exception.BusinessException;
import nnt.com.domain.base.exception.ErrorCode;
import nnt.com.domain.payment.model.entity.Refund;
import nnt.com.domain.payment.repository.RefundDomainRepository;
import nnt.com.infrastructure.persistence.payment.database.jpa.RefundInfraRepositoryJpa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RefundInfraRepositoryImpl implements RefundDomainRepository {
    RefundInfraRepositoryJpa refundInfraRepositoryJpa;

    @Override
    public Refund save(Refund refund) {
        return refundInfraRepositoryJpa.save(refund);
    }

    @Override
    public Refund update(Refund refund) {
        return refundInfraRepositoryJpa.save(refund);
    }

    @Override
    public Refund getById(Long id) {
        return refundInfraRepositoryJpa.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.REFUND_NOT_FOUND));
    }

    @Override
    public Page<Refund> getAll(int page, int size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        return refundInfraRepositoryJpa.findAll(pageable);
    }

    @Override
    public void delete(Long id) {
        refundInfraRepositoryJpa.deleteById(id);
    }
}
