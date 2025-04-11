package nnt.com.application.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.domain.aggregates.service.HomestayDomainService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class HomestayScanningScheduler {
    HomestayDomainService homestayDomainService;

    @Scheduled(fixedRate = 1000 * 60 * 30) // run every 30 minutes
    public void scanHomestaysForExpiredSubscriptions() {
        homestayDomainService.scanHomestaysForExpiredSubscriptions();
        log.info("SCANNING HOMESTAYS TO CHECK FOR EXPIRED SUBSCRIPTIONS");
    }
}
