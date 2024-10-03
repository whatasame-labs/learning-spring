package com.github.whatasame.security.jwt;

import com.github.whatasame.security.model.AuthToken;
import io.jsonwebtoken.Jwts;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private static final String AUTHORITIES_KEY = "roles";

    private final JwtSecret secret;

    public AuthToken createToken(final Authentication authentication) {
        return new AuthToken(
                createAccessToken(authentication.getPrincipal(), authentication.getAuthorities()),
                createRefreshToken(authentication.getPrincipal(), authentication.getAuthorities()));
    }

    private String createAccessToken(final Object principal, final Collection<? extends GrantedAuthority> authorities) {
        final long now = System.currentTimeMillis();

        return Jwts.builder()
                .subject(principal.toString())
                .claim(
                        AUTHORITIES_KEY,
                        authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .signWith(secret.getAccessTokenKey())
                .issuedAt(new Date(now))
                .expiration(new Date(now + secret.accessTokenExpiration()))
                .compact();
    }

    private String createRefreshToken(
            final Object principal, final Collection<? extends GrantedAuthority> authorities) {
        final long now = System.currentTimeMillis();

        return Jwts.builder()
                .subject(principal.toString())
                .claim(
                        AUTHORITIES_KEY,
                        authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .signWith(secret.getRefreshTokenKey())
                .issuedAt(new Date(now))
                .expiration(new Date(now + secret.refreshTokenExpiration()))
                .compact();
    }
}
