package nnt.com.application.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.application.service.authentication.AuthenticationAppService;
import nnt.com.infrastructure.persistence.homestay.database.elastic.UserInfraRepositoryElastic;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PRIVATE;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class TestScheduler {
    AuthenticationAppService authenticationAppService;
    UserInfraRepositoryElastic userInfraRepositoryElastic;

//    @Scheduled(fixedDelay = 5000) // milliseconds
//    public void testScheduler() {
//        Faker faker = new Faker();
//        LoginRequest loginRequest = LoginRequest.builder()
//                .email(faker.internet().emailAddress())
//                .password(faker.internet().password())
//                .build();
//        UserDocument userSearch = UserDocument.builder()
//                .email(loginRequest.getEmail())
//                .password(loginRequest.getPassword())
//                .fullName(faker.name().fullName())
//                .build();
//        userInfraRepositoryElastic.save(userSearch);
//        authenticationAppService.register(loginRequest);
//        log.info("SCHEDULED: Test scheduler executed at: {}", System.currentTimeMillis());
//    }
}
