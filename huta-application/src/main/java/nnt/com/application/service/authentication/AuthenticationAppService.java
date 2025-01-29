package nnt.com.application.service.authentication;

import jakarta.servlet.http.HttpServletResponse;
import nnt.com.domain.aggregates.model.dto.request.ChangePasswordRequest;
import nnt.com.domain.aggregates.model.dto.request.LoginRequest;
import nnt.com.domain.aggregates.model.dto.request.RegisterRequest;
import nnt.com.domain.aggregates.model.dto.response.AuthResponse;

import java.io.IOException;

public interface AuthenticationAppService {
    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(String refreshToken, HttpServletResponse response);

    void changePassword(ChangePasswordRequest request);

    void confirm(String token, HttpServletResponse response);

    String registerLandlord(String email);

    void confirmLandlord(String token, HttpServletResponse response);

    void loginGoogleAuth(HttpServletResponse response) throws IOException;

    void getOauthAccessTokenGoogle(String code, HttpServletResponse servletResponse) throws IOException;

    String forgotPassword(String email);

    String generateRandomPassword();
}
