package nnt.com.application.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.infrastructure.persistence.authentication.database.jpa.TokenInfraRepositoryJpa;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TokenScheduler {
    TokenInfraRepositoryJpa tokenRepository;

    @Scheduled(cron = "0 0 0 * * *") // second, minute, hour, day of month, month, day(s) of week
    public void removeExpiredTokens() {
        log.info("SCHEDULED: Removing expired tokens executed at: {}", System.currentTimeMillis());
        tokenRepository.deleteInvalidTokens();
    }
}
