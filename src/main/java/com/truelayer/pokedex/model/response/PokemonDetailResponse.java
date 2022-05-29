package com.truelayer.pokedex.model.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PokemonDetailResponse {

    private String name;
    private String description;
    private String habitat;
    @JsonProperty("is_legendary")
    private boolean isLegendary;

}
