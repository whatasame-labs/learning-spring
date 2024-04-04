package com.github.whatasame.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "player")
public record NestedProperties(String name, String phone, AddressProperties address) {

    public String city() {
        return address.city();
    }

    public String zip() {
        return address.zip();
    }

    @ConfigurationProperties(prefix = "address")
    record AddressProperties(String city, String zip) {}
}
