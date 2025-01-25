package nnt.com.domain.aggregates.service;

import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpServletResponse;
import nnt.com.domain.aggregates.model.entity.User;

import java.util.Map;

public interface AuthenticationDomainService {
    Map<String, String> register(String name, String email, String password);

    Map<String, String> login(String email, String password);

    Map<String, String> refreshToken(String refreshToken, HttpServletResponse response);

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