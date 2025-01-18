package nnt.com.domain.common.utils;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;

public interface JwtUtil {
    String generateAccessToken(UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    boolean isTokenValid(Jwt jwtToken, UserDetails userDetails);

    String getUsername(Jwt jwtToken);

}
