package nnt.com.domain.aggregates.service;

import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpServletResponse;
import nnt.com.domain.aggregates.model.dto.response.AuthResponse;
import nnt.com.domain.aggregates.model.entity.User;

public interface AuthenticationDomainService {
    AuthResponse register(String name, String email, String password);

    AuthResponse login(String email, String password);

    AuthResponse refreshToken(String refreshToken, HttpServletResponse response);

    void changePassword(String email, String currentPassword, String newPassword);

    void confirm(String token, HttpServletResponse response);

    String registerLandlord(String email);

    void confirmLandlord(String token, HttpServletResponse response);

    void loginGoogleAuth(HttpServletResponse response);

    void getOauthAccessTokenGoogle(String code, HttpServletResponse servletResponse);

    void getProfileDetailsGoogle(String accessToken, HttpServletResponse servletResponse);

    User checkAndCreateUser(JsonObject userInfo);

    String forgotPassword(String email);

    String generateRandomPassword();
}