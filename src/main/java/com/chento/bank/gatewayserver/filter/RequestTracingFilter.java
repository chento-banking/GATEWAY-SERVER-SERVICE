package com.chento.bank.gatewayserver.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;


@Order(1)
@Slf4j
@RequiredArgsConstructor
@Component
public class RequestTracingFilter implements GlobalFilter {

    private final FilterUtility filterUtility;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        HttpHeaders requestHeader = exchange.getRequest().getHeaders();
        if(isCorrelationIdPresent(requestHeader)) {
            log.debug("chentobank-correlation-id found in RequestTracingFilter {}",
                    filterUtility.getCorrelationId(requestHeader));
        }else {
            String correlationId = generateCorrelationId();
            exchange = filterUtility.setCorrelationId(exchange, correlationId);
            log.debug("chentobank-correlation-id generated in RequestTracingFilter {}",
                    correlationId);
        }

        return chain.filter(exchange);
    }
    private String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }

    private boolean isCorrelationIdPresent(HttpHeaders requestHeader) {
        return filterUtility.getCorrelationId(requestHeader) != null;
    }

}
