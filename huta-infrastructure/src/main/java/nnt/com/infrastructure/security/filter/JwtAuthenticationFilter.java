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
import nnt.com.infrastructure.persistence.user.database.jpa.TokenInfraRepositoryJpa;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    JwtUtil jwtUtil;
    JwtDecoder jwtDecoder;
    UserDetailsService userDetailsService;
    TokenInfraRepositoryJpa tokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isBearerToken(authHeader)) {
            String token = authHeader.substring(7);
            Jwt jwtToken = jwtDecoder.decode(token);
            String userEmail = jwtToken.getSubject();
            if (isUserNotAuthenticated()) {
                authenticateUser(request, jwtToken, userEmail);
            }
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
        boolean isTokenRevoked = tokenRepository.findByToken(jwt.getTokenValue()).stream().anyMatch(token -> !token.isRevoked());
        return isTokenValid && isTokenRevoked;
    }

    private void authenticateUser(HttpServletRequest request, Jwt jwtToken, String userEmail) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
        if (isValidToken(jwtToken, userDetails)) {
//            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                    userDetails, null, userDetails.getAuthorities());
//            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            JwtAuthenticationToken authToken = new JwtAuthenticationToken(jwtToken, authorities);
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }

}
