package nnt.com.infrastructure.persistence.booking.repository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.booking.model.entity.Booking;
import nnt.com.domain.aggregates.booking.repository.BookingDomainRepository;
import nnt.com.domain.common.exception.BusinessException;
import nnt.com.domain.common.exception.ErrorCode;
import nnt.com.infrastructure.persistence.booking.database.jpa.BookingInfraRepositoryJpa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class BookingInfraRepositoryImpl implements BookingDomainRepository {
    BookingInfraRepositoryJpa bookingInfraRepositoryJpa;

    @Override
    public Booking save(Booking booking) {
        return bookingInfraRepositoryJpa.save(booking);
    }

    @Override
    public Booking update(Booking booking) {
        return bookingInfraRepositoryJpa.save(booking);
    }

    @Override
    public Booking getById(Long id) {
        return bookingInfraRepositoryJpa.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.BOOKING_NOT_FOUND));
    }

    @Override
    public Page<Booking> getAll(int page, int size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        return bookingInfraRepositoryJpa.findAll(pageable);
    }

    @Override
    public void delete(Long id) {
        bookingInfraRepositoryJpa.deleteById(id);
    }
}
