package nnt.com.application.scheduler;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.domain.aggregates.model.dto.request.RatingRequest;
import nnt.com.domain.aggregates.model.entity.Role;
import nnt.com.domain.aggregates.model.entity.User;
import nnt.com.domain.aggregates.service.AuthenticationDomainService;
import nnt.com.domain.aggregates.service.HomestayDomainService;
import nnt.com.domain.aggregates.service.UserDomainService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class RecommendScheduler {
    UserDomainService userDomainService;
    AuthenticationDomainService authenticationDomainService;
    HomestayDomainService homestayDomainService;
    PasswordEncoder passwordEncoder;

    // Tạo dữ liệu đánh giá tự động cho thuật toán gợi ý
    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void autoRateHomestay() {
        Faker faker = new Faker();

        User user = createUser(faker);

        ratingHomestays(user, faker);
    }

    private User createUser(Faker faker) {
        User user = userDomainService.save(User.builder()
                .fullName(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .password(passwordEncoder.encode("Demo@123"))
                .role(Role.builder().role("USER").build())
                .build());
        return userDomainService.save(user);
    }

    private void ratingHomestays(User user, Faker faker) {
        AtomicInteger count = new AtomicInteger();
        homestayDomainService.getAllHomestay().forEach(homestay -> {
            if (faker.bool().bool()) { // 50% chance to rate
                homestayDomainService.ratingHomestay(homestayDomainService.getById(homestay.getId()), user, RatingRequest.builder()
                        .rating(faker.number().numberBetween(1, 5))
                        .comment(faker.lorem().sentence())
                        .build());
                count.getAndIncrement();
            }
        });
        log.info("User {} has rated {} homestays", user.getEmail(), count.get());
    }
}
