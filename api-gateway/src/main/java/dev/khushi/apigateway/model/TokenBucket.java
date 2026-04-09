package dev.khushi.apigateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenBucket {
    private int capacity;
    private int tokens;
    private long lastRefillTimestamp;
    private int refillRate;
}
