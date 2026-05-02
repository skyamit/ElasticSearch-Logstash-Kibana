package com.amit_codes.order_service.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class OrderService {
    @Value("${user.all}")
    private String USER_ALL;

    private final RestTemplate restTemplate;

    @Autowired
    public OrderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String createOrder() {
        String traceId = MDC.get("traceId");

        HttpHeaders headers = new HttpHeaders();
        headers.set("traceId", traceId);

        HttpEntity<HttpHeaders> entity = new HttpEntity<>(headers);

        log.info("Calling user-service with traceId: {}", traceId);

        restTemplate.exchange(
                USER_ALL,
                HttpMethod.POST,
                entity,
                String.class
        );

        return "Order created";
    }
}
