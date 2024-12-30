package nnt.com.domain.authentication.service.impl;

import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.authentication.model.entity.Token;
import nnt.com.domain.authentication.model.entity.User;
import nnt.com.domain.authentication.model.enums.RoleType;
import nnt.com.domain.authentication.model.enums.TokenType;
import nnt.com.domain.authentication.repository.RoleDomainRepository;
import nnt.com.domain.authentication.repository.TokenDomainRepository;
import nnt.com.domain.authentication.repository.UserDomainRepository;
import nnt.com.domain.authentication.service.AuthenticationDomainService;
import nnt.com.domain.base.exception.BusinessException;
import nnt.com.domain.base.exception.ErrorCode;
import nnt.com.domain.base.utils.JwtUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationDomainServiceImpl implements AuthenticationDomainService {
    UserDomainRepository userDomainRepository;
    RoleDomainRepository roleDomainRepository;
    PasswordEncoder passwordEncoder;
    JwtUtil jwtUtil;
    TokenDomainRepository tokenDomainRepository;


    @Override
    public Map<String, String> register(String email, String password) {
        if (userDomainRepository.existsUserByEmail(email)) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(roleDomainRepository.findByRoleName(RoleType.USER.name()))
                .build();
        user = userDomainRepository.save(user);
        return generateToken(user);
    }

    private Map<String, String> generateToken(User user) {
        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        saveToken(user, accessToken);
        return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
    }

    private void saveToken(User user, String accessToken) {
        revokeTokensByUserId(user.getId());
        Token token = Token.builder()
                .user(user)
                .token(accessToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenDomainRepository.save(token);
    }

    private void revokeTokensByUserId(long userId) {
        tokenDomainRepository.revokeTokensByUserId(userId);
    }

    @Override
    public Map<String, String> login(String email, String password) {
        User user = userDomainRepository.findByEmail(email);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }
        return generateToken(user);
    }

    @Override
    public Map<String, String> refreshToken(String refreshToken, HttpServletResponse response) {
        String username = jwtUtil.extractUsername(refreshToken);
        User user = userDomainRepository.findByEmail(username);
        if (!jwtUtil.isValidToken(refreshToken, user)) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }
        return generateToken(user);
    }

    @Override
    public void changePassword(String email, String currentPassword, String newPassword) {
        User user = userDomainRepository.findByEmail(email);
        boolean isValid = SecurityContextHolder.getContext().getAuthentication().getName().equals(email);
        if (!isValid) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
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
