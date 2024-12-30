package nnt.com.infrastructure.config.security;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Validated
@ConfigurationProperties(prefix = "application.security.rsa")
public record RSAKeyRecord(
        @NotNull RSAPrivateKey rsaPrivateKey,
        @NotNull RSAPublicKey rsaPublicKey
) {
}
