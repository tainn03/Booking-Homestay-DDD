package nnt.com.domain.aggregates.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.entity.Booking;
import nnt.com.domain.aggregates.repository.BookingDomainRepository;
import nnt.com.domain.aggregates.service.BookingDomainService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class BookingDomainServiceImpl implements BookingDomainService {
    BookingDomainRepository bookingDomainRepository;

    @Override
    public Booking save(Booking booking) {
        return bookingDomainRepository.save(booking);
    }

    @Override
    public Booking update(Booking booking) {
        return bookingDomainRepository.save(booking);
    }

    @Override
    public Booking getById(Long id) {
        return bookingDomainRepository.getById(id);
    }

    @Override
    public Page<Booking> getAll(int page, int size, String sort, String direction) {
        return bookingDomainRepository.getAll(page, size, sort, direction);
    }

    @Override
    public void delete(Long id) {
        bookingDomainRepository.delete(id);
    }
}
