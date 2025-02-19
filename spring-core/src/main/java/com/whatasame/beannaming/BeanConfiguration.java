package com.whatasame.beannaming;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

interface Animal {}

interface Policy {}

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

    @Bean(name = "dog")
    public Animal dog() {
        return new Dog();
    }

    @Bean(name = {"cat", "kitty", "neko"})
    public Animal cat() {
        return new Cat();
    }

    @Bean
    @Primary
    public Policy fixedPolicy() {
        return new FixedPolicy();
    }

    @Bean
    public Policy variablePolicy() {
        return new VariablePolicy();
    }
}

class DefaultBean {}

class NamedBean {}

class Dog implements Animal {}

class Cat implements Animal {}

class FixedPolicy implements Policy {}

class VariablePolicy implements Policy {}

@Component
class Circus {

    private final Animal dog;
    private final Animal cat;
    private final Policy policy;

    public Circus(@Qualifier("dog") final Animal dog, @Qualifier("cat") final Animal cat, final Policy policy) {
        this.dog = dog;
        this.cat = cat;
        this.policy = policy;
    }

    public Policy getPolicy() {
        return this.policy;
    }
}
