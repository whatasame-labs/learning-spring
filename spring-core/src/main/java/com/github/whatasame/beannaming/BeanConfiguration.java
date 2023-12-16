package com.github.whatasame.beannaming;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public DefaultBean defaultBean() {
        return new DefaultBean();
    }

    @Bean("super-duper-bean")
    public NamedBean namedBean() {
        return new NamedBean();
    }
}

class DefaultBean {}

class NamedBean {}
