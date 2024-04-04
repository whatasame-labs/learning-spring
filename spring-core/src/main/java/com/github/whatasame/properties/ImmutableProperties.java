package com.github.whatasame.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "immutable")
public class ImmutableProperties {

    private final String username;
    private final String password;

    public ImmutableProperties(final String username, final String password) {
        this.username = username;
        this.password = password;
    }
}
