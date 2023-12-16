package com.github.whatasame.beannaming;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

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

    @Bean(name = "dog")
    public Animal dog() {
        return new Dog();
    }

    @Bean(name = "cat")
    public Animal cat() {
        return new Cat();
    }
}

class DefaultBean {}

class NamedBean {}

class Dog implements Animal {}

class Cat implements Animal {}

@Component
class Circus {

    private final Animal dog;
    private final Animal cat;

    public Circus(@Qualifier("dog") final Animal dog, @Qualifier("cat") final Animal cat) {
        this.dog = dog;
        this.cat = cat;
    }
}
