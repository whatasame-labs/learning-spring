package com.github.whatasame.testconatiners.redis;

import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.utility.DockerImageName;

public class RedisContainerExtension implements BeforeAllCallback {

    static final RedisContainer REDIS_CONTAINER =
            new RedisContainer(DockerImageName.parse("redis:latest")).withExposedPorts(6379);

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