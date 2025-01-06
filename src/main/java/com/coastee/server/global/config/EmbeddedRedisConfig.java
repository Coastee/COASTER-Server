package com.coastee.server.global.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

import java.io.File;
import java.util.Objects;

@Profile("local")
@Configuration
@Slf4j
public class EmbeddedRedisConfig {
    @Value("${spring.data.redis.port}")
    private int redisPort;
    private RedisServer redisServer;

    @PostConstruct
    public void start() {
        if (isArmArchitecture()) {
            log.info("ARM Architecture");
            redisServer = new RedisServer(Objects.requireNonNull(getRedisServerExecutable()), redisPort);
        } else {
            redisServer = new RedisServer(redisPort);
        }
        redisServer.start();
    }

    @PreDestroy
    public void stop() {
        redisServer.stop();
    }

    private File getRedisServerExecutable() {
        try {
            return new File("src/main/resources/binary/redis/redis-server-7.2.3-mac-arm64");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isArmArchitecture() {
        return System.getProperty("os.arch").contains("aarch64");
    }
}
