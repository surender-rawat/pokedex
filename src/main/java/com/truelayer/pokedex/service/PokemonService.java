package com.truelayer.pokedex.service;

import com.truelayer.pokedex.model.PokemonInfo;


/**
 * Pokemon information Interface
 */
public interface PokemonService {
    PokemonInfo pokemonInfo(String name);
    PokemonInfo getPokemonTranslatedInfo(String name);

}
