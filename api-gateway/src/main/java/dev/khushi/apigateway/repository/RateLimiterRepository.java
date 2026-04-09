    package dev.khushi.apigateway.repository;

    import reactor.core.publisher.Mono;

    public interface RateLimiterRepository {
        Mono<String> get(String key);
        Mono<Boolean> save(String key, String value);

        // New atomic creation method
        Mono<Boolean> createIfAbsent(String key, String value, long ttlSeconds);
    }
