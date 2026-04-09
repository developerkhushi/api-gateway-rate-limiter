package dev.khushi.apigateway.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RedisRateLimiterRepository implements RateLimiterRepository {

    private final ReactiveStringRedisTemplate redisTemplate;

    @Override
    public Mono<String> get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Mono<Boolean> save(String key, String value) {
        return redisTemplate.opsForValue()
                .set(key, value, Duration.ofSeconds(60)); // bucket lives for 60 seconds
    }

    @Override
    public Mono<Boolean> createIfAbsent(String key, String value, long ttlSeconds) {
        return redisTemplate.opsForValue()
                .setIfAbsent(key, value, Duration.ofSeconds(ttlSeconds));
    }
}
