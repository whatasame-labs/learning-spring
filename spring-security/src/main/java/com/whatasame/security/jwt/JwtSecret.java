package com.whatasame.security.jwt;

import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtSecret(
        String accessTokenSecret, String refreshTokenSecret, long accessTokenExpiration, long refreshTokenExpiration) {

    public SecretKey getAccessTokenKey() {
        return Keys.hmacShaKeyFor(accessTokenSecret.getBytes());
    }

    public SecretKey getRefreshTokenKey() {
        return Keys.hmacShaKeyFor(refreshTokenSecret.getBytes());
    }
}
