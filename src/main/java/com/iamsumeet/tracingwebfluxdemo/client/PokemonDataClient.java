package com.iamsumeet.tracingwebfluxdemo.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class PokemonDataClient {

    private final WebClient webClient;

    @Autowired
    public PokemonDataClient(@Qualifier("pokemonDataWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> fetchPokemonColour(String name) {
        return webClient.get()
                .uri("/getPokemonColour/{name}",name)
                .retrieve()
                .bodyToMono(String.class);
    }

}
