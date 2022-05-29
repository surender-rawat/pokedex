package com.truelayer.pokedex.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONPropertyName;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonSerialize
@JsonDeserialize
public class PokemonSpecies extends PokemonBasicInfo{

    @JsonProperty(value="is_legendary")
    private boolean legendary;
    private Habitant habitat;
    private List<FlavorTextEntry> flavorTextEntries;



    public static class Habitant{
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class FlavorTextEntry{
        private String flavorText;
        private String translatedText;

        public String getTranslatedText() {
            return translatedText;
        }

        public void setTranslatedText(String translatedText) {
            this.translatedText = translatedText;
        }

        public String getFlavorText() {
            return flavorText;
        }

        public void setFlavorText(String flavorText) {
            this.flavorText = flavorText;
        }
    }

}
