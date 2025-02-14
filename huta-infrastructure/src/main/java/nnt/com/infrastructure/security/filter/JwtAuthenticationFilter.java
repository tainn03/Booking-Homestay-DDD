package nnt.com.infrastructure.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.domain.shared.utils.JwtUtil;
import nnt.com.infrastructure.cache.redis.RedisCache;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    JwtDecoder jwtDecoder;
    UserDetailsService userDetailsService;
    RedisCache redisCache;
    JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (isBearerToken(authHeader)) {
                String token = authHeader.substring(7);
                Jwt jwtToken = jwtDecoder.decode(token);
                String userEmail = jwtToken.getSubject();
                if (isUserNotAuthenticated()) {
                    authenticateUser(request, jwtToken, userEmail);
                }
            }
        } catch (Exception e) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"code\": 500, \"message\": \"" + e.getMessage() + "\", \"timestamp\": \"" + java.time.LocalDateTime.now() + "\"}");
            response.getWriter().flush();
        }
        filterChain.doFilter(request, response);
    }

    private boolean isBearerToken(String authHeader) {
        return authHeader != null && authHeader.startsWith("Bearer ");
    }

    private boolean isUserNotAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private boolean isValidToken(Jwt jwt, UserDetails userDetails) {
        boolean isTokenValid = jwtUtil.isTokenValid(jwt, userDetails);
        boolean isTokenRevoked = Objects.equals(redisCache.getObject(jwt.getSubject() + ":jwt", String.class), jwt.getTokenValue());
        return isTokenValid && isTokenRevoked;
    }

    private void authenticateUser(HttpServletRequest request, Jwt jwtToken, String userEmail) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
        if (isValidToken(jwtToken, userDetails)) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }

}
