package com.truelayer.pokedex.controller;


import com.truelayer.pokedex.exceptions.PokemonNoDataFoundException;
import com.truelayer.pokedex.model.PokemonInfo;
import com.truelayer.pokedex.model.response.PokemonDetailResponse;
import com.truelayer.pokedex.service.PokemonServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Function;

import static com.truelayer.pokedex.util.PokemonUtility.description;
import static com.truelayer.pokedex.util.PokemonUtility.translatedDescription;

/**
 * Controller class for exposing REST API of Pokemon information
 */
@Slf4j
@RestController
public class PokemonController {

    private PokemonServiceImpl service;

    @Autowired
    public PokemonController(PokemonServiceImpl service) {
        this.service = service;
    }

    /**
     * This method returns the basic Pokemon Information
     *
     * @param pokemonName
     * @return
     */
    @GetMapping(value = "/{pokemonName}")
    public ResponseEntity<PokemonDetailResponse> getPokemonDescription(@PathVariable String pokemonName) {
        PokemonDetailResponse response = null;
        log.info("calling pokemon details for pokemon - {}", pokemonName);
        PokemonInfo info = service.pokemonInfo(pokemonName);
        return getPokemonDetailResponseResponseEntity(info, description);

    }


    /**
     * This method retuns pokemon information with translated text
     *
     * @param pokemonName
     * @return
     */
    @GetMapping(value = "/translated/{pokemonName}")
    public ResponseEntity<PokemonDetailResponse> getPokemonTranslatedDescription(@PathVariable String pokemonName) {
        PokemonDetailResponse response = null;
        log.info("calling pokemon translated details for pokemon - {}", pokemonName);
        PokemonInfo info = service.getPokemonTranslatedInfo(pokemonName);
        return getPokemonDetailResponseResponseEntity(info, translatedDescription);

    }

    private ResponseEntity<PokemonDetailResponse> getPokemonDetailResponseResponseEntity(PokemonInfo info, Function<PokemonInfo, String> description) {
        PokemonDetailResponse response;
        if (null == info) {
            throw new PokemonNoDataFoundException("No Data Found");
        }
        response = PokemonDetailResponse.builder().name(info.getName()).description(description.apply(info)).habitat(info.getPokemonSpecies().getHabitat().getName()).isLegendary(info.getPokemonSpecies().isLegendary()).name(info.getName()).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
