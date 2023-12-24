package com.github.whatasame.testconatiners.redis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@RedisTest
@SpringBootTest
class ExtensionTest {

    @Autowired ProductService productService;

    @Test
    @DisplayName("Annotation 기반으로 Redis 컨테이너의 생명주기를 관리한다.")
    void manageRedisContainerUsingAnnotation() {
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
