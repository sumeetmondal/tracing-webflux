package com.iamsumeet.tracingwebfluxdemo.config;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Configuration
@EnableWebFlux
public class WebClientConfig implements WebFluxConfigurer {

    @Bean
    @Qualifier("pokemonDataWebClient")
    public WebClient getWebClient(HeaderExchange headerExchange) {
        return WebClient.builder()
                .baseUrl("http://localhost:3000")
                .filter(headerExchange)
                .filter(logRequest())
                .filter(logResponse())
                .filter(mdcCopyFilter)
                .build();
    }

    private ExchangeFilterFunction logRequest() {
        return (request, next) -> {
            log.info("Request method=[{}] url=[{}] headers=[{}]"
                    , request.method()
                    , request.url()
                    , request.headers().toSingleValueMap());
            return next.exchange(request);
        };
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(response -> {
            log.info("Response httpStatus=[{}] headers=[{}]"
                    , response.statusCode()
                    , response.headers().asHttpHeaders().toSingleValueMap()
            );
            return Mono.just(response);
        });
    }

    private static ExchangeFilterFunction mdcCopyFilter = (request, next) -> {
        Map<String, String> map = MDC.getCopyOfContextMap();
        return next.exchange(request).doOnNext(value -> {
                    if (map != null)
                        MDC.setContextMap(map);
                });
    };

    @Component
    static class HeaderExchange implements ExchangeFilterFunction {
        @Override
        public Mono<ClientResponse> filter(ClientRequest clientRequest, ExchangeFunction exchangeFunction) {
            return Mono.deferContextual(context -> {
                ClientRequest newRequest = ClientRequest
                        .from(clientRequest)
                        .headers(httpHeaders -> httpHeaders.add("TRACE_ID", context.get("TRACE_ID")))
                        .build();
                return exchangeFunction.exchange(newRequest);
            });
        }
    }
}
