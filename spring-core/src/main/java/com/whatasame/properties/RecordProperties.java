package com.whatasame.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "record")
public record RecordProperties(String username, String password) {}
