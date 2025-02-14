package nnt.com.domain.shared.utils;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;

public interface JwtUtil {
    String generateAccessToken(UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    String generateJwtToken(UserDetails userDetails, long tokenDurationInSeconds);

    boolean isTokenValid(Jwt jwtToken, UserDetails userDetails);

    String getUsername(Jwt jwtToken);
}
