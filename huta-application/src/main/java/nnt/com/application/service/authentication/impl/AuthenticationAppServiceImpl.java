package nnt.com.application.service.authentication.impl;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.application.brokerMQ.producer.MailProducerImpl;
import nnt.com.application.service.authentication.AuthenticationAppService;
import nnt.com.domain.aggregates.model.dto.request.ChangePasswordRequest;
import nnt.com.domain.aggregates.model.dto.request.LoginRequest;
import nnt.com.domain.aggregates.model.dto.request.RegisterRequest;
import nnt.com.domain.aggregates.model.dto.response.AuthResponse;
import nnt.com.domain.aggregates.service.AuthenticationDomainService;
import nnt.com.domain.aggregates.service.UserDomainService;
import nnt.com.domain.shared.utils.StringUtil;
import nnt.com.infrastructure.cache.redis.RedisCache;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationAppServiceImpl implements AuthenticationAppService {
    AuthenticationDomainService authenticationDomainService;
    UserDomainService userDomainService;
    RedisCache redisCache;
    MailProducerImpl mailProducer;


    @Override
    public AuthResponse register(RegisterRequest request) {
        AuthResponse result = authenticationDomainService.register(request.getName(), request.getEmail(), request.getPassword());
        return createAuthResponse(request.getEmail(), result);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        if (request.getPassword().equals(redisCache.getObject(request.getEmail() + ":pwd", String.class))) {
            AuthResponse result = authenticationDomainService.generateToken(userDomainService.getByEmail(request.getEmail()));
            return createAuthResponse(request.getEmail(), result);
        }
        AuthResponse result = authenticationDomainService.login(request.getEmail(), request.getPassword());
        return createAuthResponse(request.getEmail(), result);
    }

    private AuthResponse createAuthResponse(String email, AuthResponse result) {
        String accessToken = result.getAccessToken();
        String refreshToken = result.getRefreshToken();
        redisCache.setObject(email + ":jwt", accessToken, 30L, TimeUnit.DAYS);
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthResponse refreshToken(String refreshToken, HttpServletResponse response) {
        AuthResponse result = authenticationDomainService.refreshToken(refreshToken, response);
        return AuthResponse.builder()
                .accessToken(result.getAccessToken())
                .refreshToken(refreshToken) // trả về refreshToken cũ
                .build();
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        if (request.getCurrentPassword().equals(redisCache.getObject(request.getEmail() + ":pwd", String.class))) {
            authenticationDomainService.changePassword(request.getEmail(), request.getCurrentPassword(), request.getNewPassword(), false);
            redisCache.delete(request.getEmail() + ":pwd");
            return;
        }
        authenticationDomainService.changePassword(request.getEmail(), request.getCurrentPassword(), request.getNewPassword(), true);
    }

    @Override
    public void confirm(String token, HttpServletResponse response) {

    }

    @Override
    public void registerLandlord(String email) {
        String name = authenticationDomainService.registerLandlord(email);
        mailProducer.sendRegisterMail(email, name);
    }

    @Override
    public void confirmLandlord(String token, HttpServletResponse response) {

    }

    @Override
    public void loginGoogleAuth(HttpServletResponse response) throws IOException {
        authenticationDomainService.loginGoogleAuth(response);
    }

    @Override
    public void getOauthAccessTokenGoogle(String code, HttpServletResponse servletResponse) throws IOException {
        authenticationDomainService.getOauthAccessTokenGoogle(code, servletResponse);
    }

    @Override
    public void forgotPassword(String email) {
        String password = StringUtil.generateRandomPassword();
        mailProducer.sendForgotPasswordMail(email, password);
        redisCache.setObject(email + ":pwd", password, 5L, TimeUnit.MINUTES);
    }
}
