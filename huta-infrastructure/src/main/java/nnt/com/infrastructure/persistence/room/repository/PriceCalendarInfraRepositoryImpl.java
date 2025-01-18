package nnt.com.infrastructure.persistence.room.repository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.common.exception.BusinessException;
import nnt.com.domain.common.exception.ErrorCode;
import nnt.com.domain.aggregates.room.model.entity.PriceCalendar;
import nnt.com.domain.aggregates.room.repository.PriceCalendarDomainRepository;
import nnt.com.infrastructure.persistence.room.database.jpa.PriceCalendarInfraRepositoryJpa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class PriceCalendarInfraRepositoryImpl implements PriceCalendarDomainRepository {
    PriceCalendarInfraRepositoryJpa priceCalendarInfraRepositoryJpa;

    @Override
    public PriceCalendar save(PriceCalendar priceCalendar) {
        return priceCalendarInfraRepositoryJpa.save(priceCalendar);
    }

    @Override
    public PriceCalendar update(PriceCalendar priceCalendar) {
        return priceCalendarInfraRepositoryJpa.save(priceCalendar);
    }

    @Override
    public PriceCalendar getById(Long id) {
        return priceCalendarInfraRepositoryJpa.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.PRICE_CALENDAR_NOT_FOUND));
    }

    @Override
    public Page<PriceCalendar> getAll(int page, int size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        return priceCalendarInfraRepositoryJpa.findAll(pageable);
    }

    @Override
    public void delete(Long id) {
        priceCalendarInfraRepositoryJpa.deleteById(id);
    }
}
