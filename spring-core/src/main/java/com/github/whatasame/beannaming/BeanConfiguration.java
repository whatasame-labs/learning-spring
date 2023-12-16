package com.github.whatasame.beannaming;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

interface Animal {}

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

    @Bean
    public Animal dog() {
        return new Dog();
    }

    @Bean
    public Animal cat() {
        return new Cat();
    }
}

class DefaultBean {}

class NamedBean {}

class Dog implements Animal {}

class Cat implements Animal {}
