# Redisson on Spring Data Redis configuration

## Manual configuration of RedisConnectionFactory

When using redisson-spring-data, you can configure `RedisConnectionFactory` manually.

```java
@Configuration
public class RedissonConfig {

    @Bean
    public RedissonConnectionFactory redissonConnectionFactory(final RedissonClient redisson) {
        return new RedissonConnectionFactory(redisson);
    }

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        final Config config = new Config();

        config.useSingleServer()
                .setAddress("redis://localhost:6379");

        return Redisson.create(config);
    }
}
```

In this case, next test show up `RedissonConnectionFactory` is used.

```java
@Test
void printRedisTemplateImplClass() {
    final var redisConnectionFactory = redisTemplate.getConnectionFactory();

    // redisConnectionFactory = RedissonConnectionFactory
    System.out.println("redisConnectionFactory = " + redisConnectionFactory.getClass().getSimpleName());
}
```

Without RedissonConnectionFactory bean, it will be `LettuceConnectionFactory` which is default implementation of
`RedisConnectionFactory`.

```java
@Test
void printRedisTemplateImplClass() {
    final var redisConnectionFactory = redisTemplate.getConnectionFactory();

    // redisConnectionFactory = LettuceConnectionFactory
    System.out.println("redisConnectionFactory = " + redisConnectionFactory.getClass().getSimpleName());
}
```

## Autoconfiguration of RedisConnectionFactory

When using redisson-spring-boot-starter, `RedissonConnectionFactory` is automatically configured.

So, we don't need to configure `RedissonConnectionFactory` manually.

```java
@Configuration
public class RedissonConfig {

//    @Bean
//    public RedissonConnectionFactory redissonConnectionFactory(final RedissonClient redisson) {
//        return new RedissonConnectionFactory(redisson);
//    }

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        final Config config = new Config();

        config.useSingleServer()
                .setAddress("redis://localhost:6379");

        return Redisson.create(config);
    }
}
```

In this case, next test show up `RedissonConnectionFactory` is used.

```java
@Test
void printRedisTemplateImplClass() {
    final var redisConnectionFactory = redisTemplate.getConnectionFactory();

    // redisConnectionFactory = RedissonConnectionFactory
    System.out.println("redisConnectionFactory = " + redisConnectionFactory.getClass().getSimpleName());
}
```

## Reference
* [Integration with Spring - Redisson Reference Guide](https://redisson.org/docs/integration-with-spring/)
