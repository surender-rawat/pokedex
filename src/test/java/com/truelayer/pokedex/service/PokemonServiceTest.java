package com.truelayer.pokedex.service;

import com.truelayer.pokedex.client.PokemonClient;
import com.truelayer.pokedex.client.TextTranslatorClient;
import com.truelayer.pokedex.exceptions.PokemonException;
import com.truelayer.pokedex.model.PokemonInfo;
import com.truelayer.pokedex.model.PokemonSpecies;
import com.truelayer.pokedex.model.Translator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(MockitoExtension.class)
public class PokemonServiceTest {

    @InjectMocks
    PokemonService service;

    @Mock
    PokemonClient pokemonClient;

    @Mock
    TextTranslatorClient translatorClient;

    @Test
    @DisplayName("Should return Pokeman Basic information")
    public void shouldReturnPokemonInfo(){
        //client.getPokemonSpecies(name)
        Mockito.when(pokemonClient.getPokemonSpecies("ssr")).thenReturn(getPokemonSpecies("ssr","cave",true));
        PokemonInfo info = service.pokemonInfo("ssr");
        assertThat(info.getPokemonSpecies().isLegendary()).isTrue();
        assertThat(info.getName().equals("ssr")).isTrue();
        assertThat(info.getPokemonSpecies().getHabitat().getName().equals("cave")).isTrue();
        assertThat(info.getPokemonSpecies().getFlavorTextEntries().get(0).getFlavorText().equals("test text")).isTrue();
    }

    @Test
    @DisplayName("Should return Pokeman  information with translated text using yoda for habitat cave")
    public void shouldReturnPokemonTranslatedInfoUsingYodaForHabitatCave(){
        Mockito.when(pokemonClient.getPokemonSpecies("ssr")).thenReturn(getPokemonSpecies("ssr","cave",false));
        Mockito.when(translatorClient.getTranslatorText("test text", Translator.YODA)).thenReturn("YODA TEXT");
        PokemonInfo info =service.getPokemonTranslatedInfo("ssr");

        assertThat(info.getPokemonSpecies().isLegendary()).isFalse();
        assertThat(info.getName().equals("ssr")).isTrue();
        assertThat(info.getPokemonSpecies().getHabitat().getName().equals("cave")).isTrue();
        assertThat(info.getPokemonSpecies().getFlavorTextEntries().get(0).getTranslatedText().equals("YODA TEXT")).isTrue();
    }

    @Test
    @DisplayName("Should return Pokeman  information with translated text using yoda for legendary")
    public void shouldReturnPokemonTranslatedInfoUsingYodaForLegendary(){
        Mockito.when(pokemonClient.getPokemonSpecies("ssr")).thenReturn(getPokemonSpecies("ssr","forest",true));
        Mockito.when(translatorClient.getTranslatorText("test text", Translator.YODA)).thenReturn("YODA TEXT");
        PokemonInfo info =service.getPokemonTranslatedInfo("ssr");
        assertThat(info.getPokemonSpecies().isLegendary()).isTrue();
        assertThat(info.getName().equals("ssr")).isTrue();
        assertThat(info.getPokemonSpecies().getHabitat().getName().equals("forest")).isTrue();
        assertThat(info.getPokemonSpecies().getFlavorTextEntries().get(0).getTranslatedText().equals("YODA TEXT")).isTrue();
    }

    @Test
    @DisplayName("Should return Pokeman  information with translated text using Shakespeare")
    public void shouldReturnPokemonTranslatedInfoUsingShakespeare(){
        Mockito.when(pokemonClient.getPokemonSpecies("ssr")).thenReturn(getPokemonSpecies("ssr","forest",false));
        Mockito.when(translatorClient.getTranslatorText("test text", Translator.SHAKESPEARE)).thenReturn("SHAKESPEARE TEXT");
        PokemonInfo info =service.getPokemonTranslatedInfo("ssr");
        assertThat(info.getPokemonSpecies().isLegendary()).isFalse();
        assertThat(info.getName().equals("ssr")).isTrue();
        assertThat(info.getPokemonSpecies().getHabitat().getName().equals("forest")).isTrue();
        assertThat(info.getPokemonSpecies().getFlavorTextEntries().get(0).getTranslatedText().equals("SHAKESPEARE TEXT")).isTrue();
    }

    @Test
    @DisplayName("Should return Pokeman  information with translated text when no translation available")
    public void shouldReturnPokemonTranslatedInfoUsingShakespeareWhenNoTranslationAvailable(){
        Mockito.when(pokemonClient.getPokemonSpecies("ssr")).thenReturn(getPokemonSpecies("ssr","forest",false));
        Mockito.when(translatorClient.getTranslatorText("test text", Translator.SHAKESPEARE)).thenReturn("");
        PokemonInfo info =service.getPokemonTranslatedInfo("ssr");
        assertThat(info.getPokemonSpecies().isLegendary()).isFalse();
        assertThat(info.getName().equals("ssr")).isTrue();
        assertThat(info.getPokemonSpecies().getHabitat().getName().equals("forest")).isTrue();
        assertThat(info.getPokemonSpecies().getFlavorTextEntries().get(0).getTranslatedText().equals("test text")).isTrue();
    }

    @Test
    @DisplayName("Should throw exception for invalid pokemon name")
    public void throwExceptionForInValidPokemonName(){
        //client.getPokemonSpecies(name)
        Mockito.when(pokemonClient.getPokemonSpecies("ssr")).thenThrow(new PokemonException("no data found"));

        Exception exception = assertThrows(PokemonException.class, () -> {
            PokemonInfo info = service.pokemonInfo("ssr");
        });
        String expectedMessage = "no data found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }


    private PokemonSpecies getPokemonSpecies(String name,String habitat,boolean isLegeandry){
        PokemonSpecies species = new PokemonSpecies();
        PokemonSpecies.Habitant habitant = new PokemonSpecies.Habitant();
        habitant.setName(habitat);
        PokemonSpecies.FlavorTextEntry flavorTextEntry = new PokemonSpecies.FlavorTextEntry();
        flavorTextEntry.setFlavorText("test text");
        species.setHabitat(habitant);
        species.setLegendary(isLegeandry);
        species.setName(name);
        species.setFlavorTextEntries(Arrays.asList(flavorTextEntry));
        return species;
    }

}
