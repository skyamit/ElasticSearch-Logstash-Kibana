# Microservices API Gateway with ELK Logging

## Overview

This project demonstrates a microservices architecture with:

* API Gateway (Spring Cloud Gateway)
* User Service
* Order Service
* Centralized logging using ELK (Elasticsearch, Logstash, Kibana)
* Distributed tracing using traceId

---

## Architecture

```
Client
  ↓
API Gateway
  ↓
-----------------------
|   User Service      |
|   Order Service     |
-----------------------
  ↓
Logs (File)
  ↓
Filebeat
  ↓
Elasticsearch
  ↓
Kibana
```

---

## Tech Stack

* Java 17
* Spring Boot 3.x
* Spring Cloud Gateway (WebFlux)
* Docker and Docker Compose
* ELK Stack
* Filebeat

---

## Project Structure

```
root/
│
├── gateway/
├── user-service/
├── order-service/
├── elk/
│   ├── docker-compose.yml
│   ├── filebeat.yml
│
└── README.md
```

---

## Services

### API Gateway

* Single entry point for all requests
* Handles routing, logging, and traceId injection

### User Service

* Handles user-related APIs

Example:

```
POST /create
```

### Order Service

* Handles order-related APIs

Example:

```
POST /create
```

---

## API Routing

| Gateway Path   | Service       |
| -------------- | ------------- |
| /api/users/**  | User Service  |
| /api/orders/** | Order Service |

---

## Gateway Configuration

```
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: http://localhost:8082
          predicates:
            - Path=/api/users/**
          filters:
            - StripPrefix=2

        - id: order-service
          uri: http://localhost:8081
          predicates:
            - Path=/api/orders/**
          filters:
            - StripPrefix=2
```

---

## Logging and TraceId

A GlobalFilter is implemented in the gateway to:

* Log incoming requests
* Log responses
* Add traceId to headers
* Measure response time

Example log:

```
TraceId=abc123 Incoming Request → POST /api/orders/create
TraceId=abc123 Response → Status=200 Time=45ms
```

---

## Dependencies

### Gateway

```
spring-boot-starter-webflux
spring-cloud-starter-gateway
logstash-logback-encoder
```

---

## Log Configuration

### application.yml

```
logging:
  file:
    name: logs/gateway.log
```

### logback-spring.xml

* JSON logging enabled
* Rolling logs configured

---

## ELK Setup

Located in the elk folder.

Includes:

* Elasticsearch
* Kibana
* Logstash (optional)
* Filebeat

---

## Filebeat Configuration

filebeat.yml:

```
filebeat.inputs:
  - type: log
    enabled: true
    paths:
      - /var/log/app/*.log

output.elasticsearch:
  hosts: ["http://elasticsearch:9200"]
```

---

## Log Flow

```
Gateway logs → File → Filebeat → Elasticsearch → Kibana
```

---

## How to Run

### 1. Start ELK

```
cd elk
docker compose up -d
```

---

### 2. Start Services

Run each service:

```
mvn spring-boot:run
```

---

### 3. Test APIs

```
POST http://localhost:8080/api/orders/create
POST http://localhost:8080/api/users/create
```

---

### 4. Open Kibana

```
http://localhost:5601
```

---

### 5. View Logs

* Go to Discover
* Create index pattern:

```
filebeat-*
```

---

## Observability

Search logs using:

```
traceId: "abc123"
```

---

## Features Implemented

* API Gateway routing
* Global logging filter
* TraceId propagation
* Centralized logging with ELK
* JSON structured logs
* Dockerized ELK setup

---

## Important Notes

* Gateway uses WebFlux (reactive)
* Do not include spring-boot-starter-web
* Ensure compatible Spring Boot and Spring Cloud versions

---

## Future Enhancements

* Service discovery (Eureka)
* JWT authentication
* Distributed tracing (Zipkin)
* Metrics (Prometheus and Grafana)

---

## Conclusion

This project demonstrates a production-style microservices setup with API Gateway, centralized logging, and distributed tracing.
