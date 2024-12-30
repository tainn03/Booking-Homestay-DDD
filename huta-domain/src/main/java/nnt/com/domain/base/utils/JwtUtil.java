package nnt.com.domain.base.utils;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface JwtUtil {
    String extractUsername(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    String generateAccessToken(UserDetails userDetails);

    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    String generateConfirmationToken(UserDetails userDetails);

    boolean isValidToken(String token, UserDetails userDetails);

}
