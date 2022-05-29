package com.truelayer.pokedex.controller;


import com.truelayer.pokedex.model.PokemonInfo;
import com.truelayer.pokedex.model.response.PokemonDetailResponse;
import com.truelayer.pokedex.service.PokemonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
public class PokemonController {

    private PokemonService service;

    @Autowired
    public PokemonController(PokemonService service){
        this.service = service;
    }


    @GetMapping(value="/{pokemonName}")
    public ResponseEntity<PokemonDetailResponse> getPokemonDescription(@PathVariable String pokemonName){
        PokemonDetailResponse response = null;
        log.info("calling pokemon details for pokemon - {}",pokemonName);
        PokemonInfo info = service.pokemonInfo(pokemonName);
        if(null!=info)
             response = PokemonDetailResponse.builder().name(info.getName()).description((Optional.ofNullable(info.getPokemonSpecies().getFlavorTextEntries().get(0).getFlavorText()).orElse(null))).habitat(info.getPokemonSpecies().getHabitat().getName()).isLegendary(info.getPokemonSpecies().isLegendary()).name(info.getName()).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @GetMapping(value="/translated/{pokemonName}")
    public ResponseEntity<PokemonDetailResponse> getPokemonTranslatedDescription(@PathVariable String pokemonName){
        PokemonDetailResponse response = null;
        log.info("calling pokemon details for pokemon - {}",pokemonName);
        PokemonInfo info = service.getPokemonTranslatedInfo(pokemonName);
        if(null!=info)
            response = PokemonDetailResponse.builder().name(info.getName()).description((Optional.ofNullable(info.getPokemonSpecies().getFlavorTextEntries().get(0).getTranslatedText()).orElse(null))).habitat(info.getPokemonSpecies().getHabitat().getName()).isLegendary(info.getPokemonSpecies().isLegendary()).name(info.getName()).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }
}
