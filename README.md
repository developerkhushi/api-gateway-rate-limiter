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
curl -H "X-USER-ID: khushi123" http://localhost:8081/api/test
