package nnt.com.infrastructure.persistence.room.repository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.base.exception.BusinessException;
import nnt.com.domain.base.exception.ErrorCode;
import nnt.com.domain.room.model.entity.Discount;
import nnt.com.domain.room.repository.DiscountDomainRepository;
import nnt.com.infrastructure.persistence.room.database.jpa.DiscountInfraRepositoryJpa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class DiscountInfraRepositoryImpl implements DiscountDomainRepository {
    DiscountInfraRepositoryJpa discountInfraRepositoryJpa;

    @Override
    public Discount save(Discount discount) {
        return discountInfraRepositoryJpa.save(discount);
    }

    @Override
    public Discount update(Discount discount) {
        return discountInfraRepositoryJpa.save(discount);
    }

    @Override
    public Discount getById(Long id) {
        return discountInfraRepositoryJpa.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.DISCOUNT_NOT_FOUND));
    }

    @Override
    public Page<Discount> getAll(int page, int size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        return discountInfraRepositoryJpa.findAll(pageable);
    }

    @Override
    public void delete(Long id) {
        discountInfraRepositoryJpa.deleteById(id);
    }
}
