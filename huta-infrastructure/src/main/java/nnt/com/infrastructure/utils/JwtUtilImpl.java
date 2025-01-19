package nnt.com.infrastructure.utils;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.shared.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class JwtUtilImpl implements JwtUtil {
    final ApplicationContext applicationContext;

    @Value("${application.security.jwt.expiration}")
    long JWT_EXPIRATION;
    @Value("${application.security.jwt.refresh}")
    long REFRESH_EXPIRATION;

    @Override
    public String generateAccessToken(UserDetails userDetails) {
        return generateJwtToken(userDetails, JWT_EXPIRATION);
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return generateJwtToken(userDetails, REFRESH_EXPIRATION);
    }

    private String generateJwtToken(UserDetails userDetails, long tokenDurationInSeconds) {
        JwtEncoder jwtEncoder = applicationContext.getBean(JwtEncoder.class);
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .subject(userDetails.getUsername())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(tokenDurationInSeconds, ChronoUnit.SECONDS))
                .claim("roles", userDetails.getAuthorities().stream().map(Object::toString).toArray(String[]::new))
                .build();
        JwsHeader jwsHeader = JwsHeader.with(SignatureAlgorithm.RS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claimsSet)).getTokenValue();
    }

    @Override
    public boolean isTokenValid(Jwt jwtToken, UserDetails userDetails) {
        return !isTokenExpired(jwtToken) && getUsername(jwtToken).equals(userDetails.getUsername());
    }

    @Override
    public String getUsername(Jwt jwtToken) {
        return jwtToken.getSubject();
    }

    private boolean isTokenExpired(Jwt jwtToken) {
        return Objects.requireNonNull(jwtToken.getExpiresAt()).isBefore(Instant.now());
    }
}
