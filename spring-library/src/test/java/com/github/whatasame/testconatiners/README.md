# Testcontainers

This test shows how to use [Testcontainers](https://www.testcontainers.org/) in Spring Boot. It requires Spring Boot 3.1
and Docker.

My test environment is following.

* Spring Boot 3.2.1
* Java 17
* Docker 24.0.6

In this document, we will use Redis as example. You can use other database or service like MySQL.

## How to use Testcontainers

There are serveral ways to use Testcontainers. At first, you have to add Testconatiners dependency to your project.

```groovy
testImplementation 'org.springframework.boot:spring-boot-testcontainers'
```

### static initialize block

At first, you can use static initialize block to start container.

```java
static {
    final GenericContainer<?> REDIS_CONTAINER =
            new GenericContainer<>(DockerImageName.parse("redis:latest")).withExposedPorts(6379);
    REDIS_CONTAINER.start();

    System.setProperty("spring.data.redis.host", REDIS_CONTAINER.getHost());
    System.setProperty("spring.data.redis.port", REDIS_CONTAINER.getMappedPort(6379).toString());
}
```

`System.setProperty` is needed to set host and port of container to Spring Boot application.

### Testcontainers JUnit

With Testcontainers JUnit, you can manage lifecycle of container using annotation. Before using this, you have to add
dependency.

```groovy
testImplementation 'org.testcontainers:junit-jupiter'
```

And then, you can use `@Container` annotation to manage lifecycle of container.

```java
@Container
static final GenericContainer<?> REDIS_CONTAINER =
        new GenericContainer<>(DockerImageName.parse("redis:latest")).withExposedPorts(6379);
```

```java
@DynamicPropertySource
static void setProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
    registry.add("spring.data.redis.port", REDIS_CONTAINER::getFirstMappedPort);
}
```

### @ServiceConnection

`@DynamicPropertySource` can be replaced with `@ServiceConnection` since Spring Boot 3.1.

```java
@Container
@ServiceConnection
static final GenericContainer<?> REDIS_CONTAINER =
        new GenericContainer<>(DockerImageName.parse("redis:latest")).withExposedPorts(6379);
```

## Annotation driven test

If you want to specify test that uses Testcontainers, define custom your annotation using `@ExtendWith`.

### Define extension class

By implementing interface extending `Extension`, you can manage JUnit lifecycle. In this case, we will use `@BeforeAll`
for starting container each test instance.

```java
public class RedisContainerExtension implements BeforeAllCallback {

    static final GenericContainer<?> REDIS_CONTAINER =
            new GenericContainer<>(DockerImageName.parse("redis:latest")).withExposedPorts(6379);

    @Override
    public void beforeAll(final ExtensionContext context) throws Exception {
        if (REDIS_CONTAINER.isRunning()) {
            return;
        }

        REDIS_CONTAINER.start();
        System.setProperty("spring.data.redis.host", REDIS_CONTAINER.getHost());
        System.setProperty(
                "spring.data.redis.port", REDIS_CONTAINER.getMappedPort(6379).toString());
    }
}
```

### Define annotation

And then, define annotation using `@ExtendWith`.

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(RedisContainerExtension.class)
public @interface RedisTest {}
```

### Use annotation

Finally, you can use annotation to specify test that uses Testcontainers.

```java
@RedisTest
@SpringBootTest
class RedisServiceTest{
    /* do your test */
}
```

## Spring Boot Testcontainers module

Spring Boot Testcontainers provides modules for several service.

### MySQL

Spring Boot Testcontainers provides `testcontainers-mysql` module.

```java
@Container
static final MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>("mysql:latest");
```

## Reference

* [Improved Testcontainers Support in Spring Boot 3.1 | Spring blog](https://spring.io/blog/2023/06/23/improved-testcontainers-support-in-spring-boot-3-1/)
* [TestContainers로 유저시나리오와 비슷한 통합테스트 만들어 보기 | Kurly Tech Blog](https://helloworld.kurly.com/blog/delivery-testContainer-apply/)
