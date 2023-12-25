package com.github.whatasame.testconatiners.redis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Testcontainers(disabledWithoutDocker = true)
@DisplayName("학습 테스트: Testcontainers Redis")
class TestcontainersRedisTest {

    @Container @ServiceConnection
    static final GenericContainer<?> REDIS_CONTAINER =
            new GenericContainer<>(DockerImageName.parse("redis:latest")).withExposedPorts(6379);

    @Autowired ProductService productService;

    /* Can be replaced to @ServiceConnection since Spring Boot 3.1 */
    //    @DynamicPropertySource
    //    static void registerRedisProperties(final DynamicPropertyRegistry registry) {
    //        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost); //
    // "spring.redis.host" for Spring Boot 2.x
    //        registry.add("spring.data.redis.port", REDIS_CONTAINER::getFirstMappedPort); //
    // "spring.redis.port" for Spring Boot 2.x
    //    }

    @Test
    @DisplayName("Testcontainers를 이용하여 Redis 컨테이너의 생명주기를 관리한다.")
    void manageRedisContainerUsingTestcontainers() {
        /* given */
        final Product macBookPro = new Product("product:1", "M3 MacBook Pro", 1_599);
        productService.createProduct(macBookPro);

        /* when */
        final Product product = productService.getProduct(macBookPro.getId());

        /* then */
        assertAll(
                () -> assertThat(product.getId()).isEqualTo(macBookPro.getId()),
                () -> assertThat(product.getName()).isEqualTo(macBookPro.getName()),
                () -> assertThat(product.getPrice()).isEqualTo(macBookPro.getPrice()));
    }
}
