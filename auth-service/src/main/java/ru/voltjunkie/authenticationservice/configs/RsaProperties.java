package ru.voltjunkie.authenticationservice.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "jwt")

public class RsaProperties {
    private String publicKey;
    private String privateKey;
    private int expiration;
    private int refreshExpirationMultiplier;

    public RsaProperties setPublicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public RsaProperties setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
        return this;
    }

    public RsaProperties setExpiration(int expiration) {
        this.expiration = expiration;
        return this;
    }

    public RsaProperties setRefreshExpirationMultiplier(int refreshExpirationMultiplier) {
        this.refreshExpirationMultiplier = refreshExpirationMultiplier;
        return this;
    }
}
