package com.github.whatasame.redisson;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class RedissonConfigTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void printRedisTemplateImplClass() {
        final var redisConnectionFactory = redisTemplate.getConnectionFactory();

        System.out.println(
                "redisConnectionFactory = " + redisConnectionFactory.getClass().getSimpleName());
    }
}
