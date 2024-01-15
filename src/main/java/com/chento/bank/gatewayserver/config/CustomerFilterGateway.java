package com.chento.bank.gatewayserver.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class CustomerFilterGateway {

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {




        return builder.routes()
                .route(p -> p
                        .path("/chentobank/account/**")
                        .filters(f -> f.rewritePath("/chentobank/account/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-RESPONSE-TIME", LocalDateTime.now().toString()))
                        .uri("lb://ACCOUNT"))

                .route(p -> p
                        .path("/chentobank/loan/**")
                        .filters(f -> f.rewritePath("/chentobank/loan/(?<segment>.*)", "/${segment}"))
                        .uri("lb://LOAN"))

                .route(p -> p
                        .path("/chentobank/card/**")
                        .filters(f -> f.rewritePath("/chentobank/card/(?<segment>.*)", "/${segment}"))
                        .uri("lb://CARD"))

                .build();
    }
}
