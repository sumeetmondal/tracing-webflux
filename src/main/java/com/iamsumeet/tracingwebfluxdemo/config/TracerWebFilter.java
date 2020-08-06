package com.iamsumeet.tracingwebfluxdemo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class TracerWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange).contextWrite(
                Context.of("TRACE_ID", Optional.ofNullable(exchange.getRequest()
                .getHeaders()
                .getFirst("TRACE_ID"))
                .orElse(UUID.randomUUID().toString())));
    }
}
