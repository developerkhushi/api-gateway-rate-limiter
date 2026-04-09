# Rate Limiter using API Gateway (Token Bucket)

## Architecture
Client → API Gateway → Rate Limiter → Service
                     ↓
                   Redis

## Modules
- api-gateway
- test-service

## Tech Stack
- Spring Boot
- Spring Cloud Gateway
- Redis
- Token Bucket Algorithm
- Docker

## Run
1. Start Redis
   docker run -p 6379:6379 redis

2. Run test-service
3. Run api-gateway

## Test
curl http://localhost:8080/api/test