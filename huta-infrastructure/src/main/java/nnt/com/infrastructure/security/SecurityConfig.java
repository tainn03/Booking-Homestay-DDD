package nnt.com.infrastructure.security;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.infrastructure.security.custom.CustomAccessDeniedHandler;
import nnt.com.infrastructure.security.custom.CustomBasicAuthenticationEntryPoint;
import nnt.com.infrastructure.security.filter.JwtAuthenticationFilter;
import nnt.com.infrastructure.security.filter.RateLimitFilter;
import nnt.com.infrastructure.security.key.RSAKeyRecord;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EnableWebSecurity
@EnableMethodSecurity
@EnableJpaAuditing(auditorAwareRef = "AuditorAwareImpl")
@EnableElasticsearchRepositories
@EnableJpaRepositories(basePackages = "nnt.com.infrastructure.persistence")
@EnableConfigurationProperties(RSAKeyRecord.class)
@EnableScheduling
public class SecurityConfig {
    JwtAuthenticationFilter jwtAuthenticationFilter;
    RateLimitFilter rateLimitFilter;
    LogoutHandler logoutHandler;
    String[] WHITE_LIST_URL = {
            "/actuator/**",
            "/api/v1/auth/public/**",
            "api/v1/perf/**",
            "/api/v1/homestays/public/**",
            "/api/v1/search/**",
            "/api/v1/general/**",
            "/api/v1/payment/**",
            "/api/v1/booking/public/**",
    };

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(WHITE_LIST_URL).permitAll()
                                .anyRequest().permitAll()
                )
                .sessionManagement(smc -> smc.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ecf -> {
                    ecf.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint());
                    ecf.accessDeniedHandler(new CustomAccessDeniedHandler());
                })
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .cors(corsConfig -> corsConfig.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                    config.addAllowedMethod("*");
                    config.addAllowedHeader("*");
                    config.setMaxAge(3600L);
                    return config;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .requiresChannel((requiresChannel) -> requiresChannel.anyRequest().requiresInsecure()) // http
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(rateLimitFilter, JwtAuthenticationFilter.class)
                .logout(logout ->
                        logout.logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> {
                                    SecurityContextHolder.clearContext();
                                    response.setStatus(200);
                                })
                )
        ;

//        http.formLogin(withDefaults());
        return http.build();
    }
}
