package nnt.com.domain.aggregates.service;

import jakarta.servlet.http.HttpServletResponse;
import nnt.com.domain.aggregates.model.dto.response.AuthResponse;
import nnt.com.domain.aggregates.model.entity.User;

import java.io.IOException;

public interface AuthenticationDomainService {
    AuthResponse register(String name, String email, String password);

    AuthResponse login(String email, String password);

    AuthResponse generateToken(User user);

    AuthResponse refreshToken(String refreshToken, HttpServletResponse response);

    void changePassword(String email, String currentPassword, String newPassword, boolean isNeedToCheck);

    void confirm(String token, HttpServletResponse response);

    String registerLandlord(String email);

    void confirmLandlord(String token, HttpServletResponse response);

    void loginGoogleAuth(HttpServletResponse response) throws IOException;

    void getOauthAccessTokenGoogle(String code, HttpServletResponse servletResponse) throws IOException;
}