package nnt.com.domain.authentication.service;

import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpServletResponse;
import nnt.com.domain.authentication.model.entity.User;

import java.util.Map;

public interface AuthenticationDomainService {
    Map<String, String> register(String fullName, String email, String password);

    Map<String, String> authenticate(String email, String password, HttpServletResponse response);

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