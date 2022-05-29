package com.truelayer.pokedex.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PokemonInfo extends  PokemonBasicInfo{

    private PokemonSpecies pokemonSpecies;
}
