package com.amit_codes.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        long startTime = System.currentTimeMillis();

        ServerHttpRequest request = exchange.getRequest();

        String method = request.getMethod().name();
        String path = request.getURI().getPath();

        String traceId = request.getHeaders().getFirst("X-Trace-Id");
        if (traceId == null) {
            traceId = UUID.randomUUID().toString();
        }

        String finalTraceId = traceId;

        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(request.mutate()
                        .header("X-Trace-Id", finalTraceId)
                        .build())
                .build();

        mutatedExchange.getResponse().getHeaders().add("X-Trace-Id", finalTraceId);

        log.info("TraceId={} Incoming Request → {} {}", finalTraceId, method, path);

        request.getHeaders().forEach((key, value) ->
                log.debug("TraceId={} Header → {} = {}", finalTraceId, key, value)
        );

        return chain.filter(mutatedExchange)
                .doOnError(error ->
                        log.error("TraceId={} Error → {} {}", finalTraceId, method, path, error)
                )
                .then(Mono.fromRunnable(() -> {
                    long timeTaken = System.currentTimeMillis() - startTime;

                    int statusCode = mutatedExchange.getResponse().getStatusCode() != null
                            ? mutatedExchange.getResponse().getStatusCode().value()
                            : 0;

                    log.info("TraceId={} Response → {} {} | Status={} | Time={}ms",
                            finalTraceId, method, path, statusCode, timeTaken);
                }));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}