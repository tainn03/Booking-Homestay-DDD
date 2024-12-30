package nnt.com.infrastructure.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.base.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class JwtUtilImpl implements JwtUtil {
    @Value("${application.security.jwt.secret}")
    String SECRET_KEY;
    @Value("${application.security.jwt.expiration}")
    long JWT_EXPIRATION;
    @Value("${application.security.jwt.refresh}")
    long REFRESH_EXPIRATION;

    // TRÍCH XUẤT THÔNG TIN NGƯỜI DÙNG TỪ JWT
    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) { // Trích xuất tất cả thông tin từ chuỗi jwt
        return Jwts.parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() { // Lấy secret key
        byte[] secretBytes = Decoders.BASE64.decode(SECRET_KEY); // Giải mã chuỗi SECRET_KEY ra thành mảng byte
        return Keys.hmacShaKeyFor(secretBytes);
    }


    // TẠO JWT MỚI DỰA TRÊN THÔNG TIN NGƯỜI DÙNG
    @Override
    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        String roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));
        extraClaims.put("roles", roles); // Thêm claim về roles
        return generateToken(extraClaims, userDetails);
    }

    @Override
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, JWT_EXPIRATION);
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) { // tạo refresh token
        return buildToken(new HashMap<>(), userDetails, REFRESH_EXPIRATION);
    }

    @Override
    public String generateConfirmationToken(UserDetails userDetails) { // tạo confirmation token
        return buildToken(new HashMap<>(), userDetails, JWT_EXPIRATION);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Thời gian hết hạn của jwt
                .addClaims(extraClaims)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // KIỂM TRA JWT CÓ HỢP LỆ HAY KHÔNG
    @Override
    public boolean isValidToken(String token, UserDetails userDetails) {
        final String email = extractUsername(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) { // Kiểm tra jwt có hết hạn hay không
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) { // Trích xuất thời gian hết hạn từ chuỗi jwt
        return extractClaim(token, Claims::getExpiration);
    }

}
