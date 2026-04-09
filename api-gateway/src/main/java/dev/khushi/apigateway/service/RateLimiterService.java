package dev.khushi.apigateway.service;

import dev.khushi.apigateway.repository.RedisRateLimiterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RateLimiterService {

    private final RedisRateLimiterRepository repository;
    private static final int CAPACITY = 1;
    private static final int REFILL_RATE = 0; // tokens per second

    public Mono<Boolean> allowRequest(String key) {
        long now = System.currentTimeMillis();
        return repository.get(key)
                .flatMap(value -> {
                    // Parse tokens and last refill time
                    String[] parts = value.split(":");
                    int tokens = Integer.parseInt(parts[0]);
                    long lastRefill = Long.parseLong(parts[1]);

                    // Calculate new tokens based on REFILL_RATE
                    long elapsed = (now - lastRefill) / 1000;
                    int refill = (int) (elapsed * REFILL_RATE);
                    tokens = Math.min(CAPACITY, tokens + refill);

                    System.out.println("Tokens left : " + tokens);
                    if (tokens > 0) {
                        tokens--; // consume 1 token
                        return repository.save(key, tokens + ":" + now)
                                .thenReturn(true);
                    } else {
                        // No tokens left, reject
                        System.out.println("Request Rejected !!");
                        return Mono.just(false);
                    }
                })
                .switchIfEmpty(createNewBucket(key, now));
    }

    // Atomic bucket creation for new users
    private Mono<Boolean> createNewBucket(String key, long now) {
        int tokens = CAPACITY - 1; // consume 1 token immediately
        String value = tokens + ":" + now;

        // Use repository atomic creation
        return repository.createIfAbsent(key, value, 60) // TTL 60 seconds
                .flatMap(created -> {
                    if (created) return Mono.just(true);  // first request allowed
                    else return Mono.just(false);         // already exists, reject
                });
    }
}