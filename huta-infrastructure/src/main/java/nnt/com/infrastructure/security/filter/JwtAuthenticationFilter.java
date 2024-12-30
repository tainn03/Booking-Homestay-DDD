package nnt.com.infrastructure.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.base.utils.JwtUtil;
import nnt.com.infrastructure.persistence.authentication.database.jpa.TokenInfraRepositoryJpa;
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

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    JwtUtil jwtUtil;
    JwtDecoder jwtDecoder;
    UserDetailsService userDetailsService;
    TokenInfraRepositoryJpa tokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final Jwt jwt;
        final String userEmail;

        if (!isBearerToken(authHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = jwtDecoder.decode(authHeader.substring(7));
        userEmail = jwtUtil.getUsername(jwt);

        if (isUserNotAuthenticated()) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            if (isValidToken(jwt, userDetails)) {
                authenticateUser(userDetails, request);
            }
        }
        doFilter(request, response, filterChain);
    }

    private boolean isBearerToken(String authHeader) {
        return authHeader != null && authHeader.startsWith("Bearer ");
    }

    private boolean isUserNotAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private boolean isValidToken(Jwt jwt, UserDetails userDetails) {
        return jwtUtil.isTokenValid(jwt, userDetails) && tokenRepository.findByToken(jwt.toString())
                .map(token -> !token.isExpired() && !token.isRevoked())
                .orElse(false);
    }

    private void authenticateUser(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        authToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

}
