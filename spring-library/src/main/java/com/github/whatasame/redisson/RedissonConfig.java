package com.github.whatasame.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Bean
    public RedissonConnectionFactory redissonConnectionFactory(final RedissonClient redisson) {
        return new RedissonConnectionFactory(redisson);
    }

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        final Config config = new Config();

        config.useSingleServer().setAddress("redis://localhost:6379");

        return Redisson.create(config);
    }
}
