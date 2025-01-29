package nnt.com.domain.aggregates.service.impl;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.dto.response.AuthResponse;
import nnt.com.domain.aggregates.model.entity.User;
import nnt.com.domain.aggregates.model.enums.RoleType;
import nnt.com.domain.aggregates.repository.RoleDomainRepository;
import nnt.com.domain.aggregates.repository.UserDomainRepository;
import nnt.com.domain.aggregates.service.AuthenticationDomainService;
import nnt.com.domain.shared.exception.BusinessException;
import nnt.com.domain.shared.exception.ErrorCode;
import nnt.com.domain.shared.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationDomainServiceImpl implements AuthenticationDomainService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationDomainServiceImpl.class);
    final UserDomainRepository userDomainRepository;
    final RoleDomainRepository roleDomainRepository;
    final PasswordEncoder passwordEncoder;
    final JwtUtil jwtUtil;
    final JwtDecoder jwtDecoder;

    @Value("${application.security.google.client-id}")
    String CLIENTID;
    @Value("${application.security.google.client-secret}")
    String CLIENT_SECRET;
    @Value("${application.security.google.redirect-uri}")
    String REDIRECT_URI;


    @Override
    public AuthResponse register(String name, String email, String password) {
        if (userDomainRepository.existsUserByEmail(email)) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        User user = User.builder()
                .fullName(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(roleDomainRepository.findByRoleName(RoleType.USER.name()))
                .lastLogin(LocalDateTime.now())
                .build();
        user = userDomainRepository.save(user);
        return generateToken(user);
    }

    @Override
    public AuthResponse generateToken(User user) {
        return AuthResponse.builder()
                .accessToken(jwtUtil.generateAccessToken(user))
                .refreshToken(jwtUtil.generateRefreshToken(user))
                .build();
    }

    @Override
    public AuthResponse login(String email, String password) {
        User user = userDomainRepository.findByEmail(email);
        if (!Objects.equals(user.getPassword(), "google") && !passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }
        return generateToken(user);
    }

    @Override
    public AuthResponse refreshToken(String refreshToken, HttpServletResponse response) {
        Jwt jwt = jwtDecoder.decode(refreshToken);
        String username = jwtUtil.getUsername(jwt);
        User user = userDomainRepository.findByEmail(username);
        if (!jwtUtil.isTokenValid(jwt, user)) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }
        return generateToken(user);
    }

    @Override
    public void changePassword(String email, String currentPassword, String newPassword, boolean isNeedToCheck) {
        User user = userDomainRepository.findByEmail(email);
        if (isNeedToCheck && !Objects.equals(user.getPassword(), "google") && !passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userDomainRepository.save(user);
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
    public void loginGoogleAuth(HttpServletResponse response) throws IOException {
        String googleAuthUrl = "https://accounts.google.com/o/oauth2/v2/auth?redirect_uri=" + REDIRECT_URI +
                "&response_type=code&client_id=" + CLIENTID +
                "&scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile+openid&access_type=offline&prompt=consent";
        response.sendRedirect(googleAuthUrl);
        log.info("Redirect to Google Auth: {}", googleAuthUrl);
    }

    @Override
    public void getOauthAccessTokenGoogle(String code, HttpServletResponse servletResponse) throws IOException {
        // Gửi yêu cầu POST đến Google để đổi code lấy token
        String response = callRestApiToGetAccessToken(code);

        // Phân tích cú pháp phản hồi JSON để lấy access token
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
        String accessToken = jsonObject.get("access_token").toString();

        // Lấy thông tin người dùng từ Google với access token
        getProfileDetailsGoogle(accessToken, servletResponse);
    }

    private String callRestApiToGetAccessToken(String code) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("client_id", CLIENTID);
        params.add("client_secret", CLIENT_SECRET);
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, httpHeaders);

        String url = "https://oauth2.googleapis.com/token";

        // Gửi yêu cầu POST đến Google để đổi code lấy token
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(url, requestEntity, String.class);
    }

    private void getProfileDetailsGoogle(String accessToken, HttpServletResponse servletResponse) throws IOException {
        ResponseEntity<String> response = callRestApiToGetProfile(accessToken);

        // Phân tích cú pháp JSON để lấy thông tin người dùng
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(response.getBody(), JsonObject.class);

        // Check and create user if not exists
        User user = checkOrCreateUser(jsonObject);

        // Điều hướng về frontend kèm theo accessToken
        String redirectUrl = "http://localhost:3000?accessToken=" + jwtUtil.generateAccessToken(user);
        servletResponse.sendRedirect(redirectUrl);
    }

    private ResponseEntity<String> callRestApiToGetProfile(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(accessToken);  // Thiết lập access token cho Bearer Auth
        HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);

        String url = "https://www.googleapis.com/oauth2/v2/userinfo";
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
    }

    private User checkOrCreateUser(JsonObject userInfo) {
        String email = userInfo.get("email").getAsString();
        try {
            User user = userDomainRepository.findByEmail(email);
            user.setLastLogin(LocalDateTime.now());
            return userDomainRepository.save(user);
        } catch (Exception e) {
            return userDomainRepository.save(User.builder()
                    .fullName(userInfo.get("name").getAsString())
                    .email(email)
                    .password("google") // Mật khẩu mặc định
                    .role(roleDomainRepository.findByRoleName(RoleType.USER.name()))
                    .lastLogin(LocalDateTime.now())
                    .avatar(userInfo.get("picture").getAsString())
                    .build());
        }
    }
}
