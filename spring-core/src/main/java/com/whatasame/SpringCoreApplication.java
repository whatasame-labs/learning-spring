package com.whatasame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SpringCoreApplication {

    public static void main(final String[] args) {
        SpringApplication.run(SpringCoreApplication.class, args);
    }
}
