package dev.khushi.apigateway.filter;

import dev.khushi.apigateway.service.RateLimiterService;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component

public class RateLimiter extends AbstractGatewayFilterFactory<RateLimiter.Config> {

    private final RateLimiterService rateLimiterService;

    public RateLimiter (RateLimiterService rateLimiterService) {
        super(Config.class);
        this.rateLimiterService = rateLimiterService;
    }

    public static class Config {}

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String user = exchange.getRequest()
                    .getHeaders()
                    .getFirst("X-USER-ID");

            if (user == null) {
                user = exchange.getRequest()
                        .getRemoteAddress()
                        .getAddress()
                        .getHostAddress();
            }

            String key = "rate_limit:" + user;

            return rateLimiterService.allowRequest(key)
                    .flatMap(allowed -> {
                        if (allowed) {
                            return chain.filter(exchange);
                        }

                        exchange.getResponse()
                                .setStatusCode(HttpStatus.TOO_MANY_REQUESTS);

                        return exchange.getResponse().setComplete();
                    });
        };
    }
}
