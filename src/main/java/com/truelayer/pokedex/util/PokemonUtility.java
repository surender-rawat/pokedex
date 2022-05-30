package com.truelayer.pokedex.util;

import com.truelayer.pokedex.model.PokemonInfo;

import java.util.function.BiFunction;
import java.util.function.Function;


/**
 * Utility class for methods and functions
 */
public class PokemonUtility {

    public static Function<PokemonInfo,String> description = (pokemonInfo -> pokemonInfo.getPokemonSpecies().getFlavorTextEntries().get(0).getFlavorText());
    public static Function<PokemonInfo,String> translatedDescription = (pokemonInfo -> pokemonInfo.getPokemonSpecies().getFlavorTextEntries().get(0).getTranslatedText());
    public static BiFunction<PokemonInfo,String,PokemonInfo> addTranslatedDescription = (pokemonInfo,text)->{
        if(null!=pokemonInfo){
            pokemonInfo.getPokemonSpecies().getFlavorTextEntries().get(0).setTranslatedText(text);
        }
        return pokemonInfo;
    };
}
