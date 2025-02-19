# Bean naming

This test case shows how works bean naming in Spring.

## Bean naming strategy

Spring uses the following rules to generate bean names.

### Default bean name

If you don't specify a bean name, Spring generates a bean name from Class name with the first letter lower-cased.

```java
@Component
public class DefaultComponent {}

@Configuration
public class BeanConfiguration {

    @Bean
    public DefaultBean defaultBean() {
        return new DefaultBean();
    }
}
```

This will generate a bean name `defaultComponent` and `defaultBean`.

### Explicit bean name

You can specify a bean name explicitly by using the `name` attribute of the `@Component` annotation.

```java
@Component("super-duper-component")
public class NamedComponent {}

@Configuration
public class BeanConfiguration {

    @Bean("super-duper-bean")
    public NamedBean namedBean() {
        return new NamedBean();
    }
}
```

This will generate a bean name `super-duper-component` and  `super-duper-bean` .

## How to find injected target bean?

### Using class type

In general, Spring will try to find a bean by type.

### Using `@Primary`

If there are multiple beans of the same type, Spring will use the bean marked with `@Primary` as the target bean.

### Using `@Qualifier`

If there are multiple beans of the same type and no bean is marked with `@Primary`, Spring will use the bean marked with
`@Qualifier` as the target bean.

### Don't use `@Qualifier` on @Bean methods

It is not necessary to use `@Qualifier` when defining a bean using the `@Bean` annotation because the bean’s name can be
explicitly specified using the name attribute or derived from the method name. Beans should be named appropriately using
either the name attribute of the `@Bean` annotation or the method name itself.

AS IS

```java
@Bean
@Qualifier("cat")
public Animal cat() {
    return new Cat();
}
```

TO BE

```java
@Bean(name = "cat")
public Animal cat() {
    return new Cat();
}
```

## Reference

- [Spring Bean Names | Baeldung](https://www.baeldung.com/spring-bean-names)
- [Fine-tuning Annotation-based Autowiring with Qualifiers | Spring documentation](https://docs.spring.io/spring-framework/reference/core/beans/annotation-config/autowired-qualifiers.html)
