package com.iamsumeet.tracingwebfluxdemo.service;


import com.iamsumeet.tracingwebfluxdemo.client.PokemonDataClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class DataService {

    private PokemonDataClient client;

    @Autowired
    public DataService(PokemonDataClient client) {
        this.client = client;
    }

    public Mono<String> fetchPokemonColour(String name) {
        return client.fetchPokemonColour(name);
    }

}
