package com.iamsumeet.tracingwebfluxdemo.controller;

import com.iamsumeet.tracingwebfluxdemo.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@Controller
public class PokemonController {

    private final DataService dataService;

    @Autowired
    public PokemonController(DataService dataService) {
        this.dataService = dataService;
    }


    @GetMapping("/getPokemonColour")
    public Mono<ResponseEntity<String>> getPokemonColour(@RequestParam("colour") String colour){
        return dataService.fetchPokemonColour(colour).map(pokemon -> new ResponseEntity<>(pokemon,HttpStatus.OK));
    }


}
