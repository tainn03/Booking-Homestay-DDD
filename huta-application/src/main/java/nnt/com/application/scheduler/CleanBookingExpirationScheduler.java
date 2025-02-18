package nnt.com.application.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.domain.aggregates.service.BookingDomainService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class CleanBookingExpirationScheduler {
    BookingDomainService bookingDomainService;

    @Scheduled(fixedRate = 1000 * 60) // run every 30 minutes
    public void cleanBookingExpiration() {
        bookingDomainService.cleanBookingExpiration();
        log.info("CLEANING EXPIRED BOOKINGS");
    }
}
