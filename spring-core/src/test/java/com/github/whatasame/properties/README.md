# Properties

Spring supports externalized configuration in a flexible way.

## @ConfigurationProperties

`@ConfigurationProperties` is a class-level annotation that allows you to bind external properties to a class. As we
bind properties to a class, we can use them as regular Java objects. It makes the code to be more readable and
maintainable.

### How to define

We can bind properties to a class by using `@ConfigurationProperties` annotation.

```java
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String name;
    private String version;
    private String description;
}
```

But there are many ways to bind property values to a class.

### @EnableConfigurationProperties

`@EnableConfigurationProperties` is a class-level annotation that allows you to register `@ConfigurationProperties`.
It is used to register `@ConfigurationProperties` annotated classes. Usually, it is used in the main class.

```java
@EnableConfigurationProperties(AppProperties.class)
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

However, it has drawback. If we have more classes with properties, we need to register all of them in the main class. It
will bother us.

### Meta-annotation

A meta-annotation like `@Configuration` allows a bean to be registered in the Spring context.

```java
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    // ...
    
    // needs getter and setter
}
```

This is not ideal for immutable classes because a setter could potentially alter our properties at any time.

### @ConfigurationPropertiesScan

`@ConfigurationPropertiesScan` is similar to `@ComponentScan`. It scans the package for `@ConfigurationProperties`
classes and registers them in the Spring context. Therefore, we don't need to update the main class if we add a new one.

```java
@SpringBootApplication
@ConfigurationPropertiesScan()
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

In addition, it is possible to make a properties class immutable using `final` or `record`.

```java
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private final String name;
    private final String version;
    private final String description;
    
    // @ConstructorBinding // it is required for more than one constructor to specify which one to use.
    public AppProperties(String name, String version, String description) {
        this.name = name;
        this.version = version;
        this.description = description;
    }
}
```

```java
@ConfigurationProperties(prefix = "app")
public record AppProperties(String name, String version, String description) {
}
```

Therefore, I recommend using `@ConfigurationPropertiesScan` and `record` to make properties class immutable.

## References

* [Guide to @ConfigurationProperties in Spring Boot - Baeldung](https://www.baeldung.com/configuration-properties-in-spring-boot)
