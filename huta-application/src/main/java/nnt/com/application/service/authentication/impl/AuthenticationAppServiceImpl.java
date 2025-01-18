package nnt.com.application.service.authentication.impl;

import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.application.model.dto.request.ChangePasswordRequest;
import nnt.com.application.model.dto.request.LoginRequest;
import nnt.com.application.model.dto.response.AuthResponse;
import nnt.com.application.service.authentication.AuthenticationAppService;
import nnt.com.domain.aggregates.user.model.entity.User;
import nnt.com.domain.aggregates.user.service.AuthenticationDomainService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationAppServiceImpl implements AuthenticationAppService {
    AuthenticationDomainService authenticationDomainService;

    @Override
    public AuthResponse register(LoginRequest request) {
        Map<String, String> result = authenticationDomainService.register(request.getEmail(), request.getPassword());
        return createAuthResponse(result);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Map<String, String> result = authenticationDomainService.login(request.getEmail(), request.getPassword());
        return createAuthResponse(result);
    }

    private AuthResponse createAuthResponse(Map<String, String> result) {
        return AuthResponse.builder()
                .accessToken(result.get("accessToken"))
                .refreshToken(result.get("refreshToken"))
                .build();
    }

    @Override
    public AuthResponse refreshToken(String refreshToken, HttpServletResponse response) {
        Map<String, String> result = authenticationDomainService.refreshToken(refreshToken, response);
        return AuthResponse.builder()
                .accessToken(result.get("accessToken"))
                .refreshToken(refreshToken) // trả về refreshToken cũ
                .build();
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        authenticationDomainService.changePassword(request.getEmail(), request.getCurrentPassword(), request.getNewPassword());
    }

    @Override
    public void confirm(String token, HttpServletResponse response) {

    }

    @Override
    public String registerLandlord(String email) {
        return "";
    }

    @Override
    public void confirmLandlord(String token, HttpServletResponse response) {

    }

    @Override
    public void loginGoogleAuth(HttpServletResponse response) {

    }

    @Override
    public void getOauthAccessTokenGoogle(String code, HttpServletResponse servletResponse) {

    }

    @Override
    public void getProfileDetailsGoogle(String accessToken, HttpServletResponse servletResponse) {

    }

    @Override
    public User checkAndCreateUser(JsonObject userInfo) {
        return null;
    }

    @Override
    public String forgotPassword(String email) {
        return "";
    }

    @Override
    public String generateRandomPassword() {
        return "";
    }
}
