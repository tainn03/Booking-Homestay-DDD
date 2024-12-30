package nnt.com.application.service.authentication;

import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpServletResponse;
import nnt.com.application.model.dto.request.ChangePasswordRequest;
import nnt.com.application.model.dto.request.LoginRequest;
import nnt.com.application.model.dto.response.AuthResponse;
import nnt.com.domain.authentication.model.entity.User;

public interface AuthenticationAppService {
    AuthResponse register(LoginRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(String refreshToken, HttpServletResponse response);

    void changePassword(ChangePasswordRequest request);

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
