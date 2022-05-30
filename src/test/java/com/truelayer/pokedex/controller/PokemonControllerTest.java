package com.truelayer.pokedex.controller;

import com.truelayer.pokedex.exceptions.PokemonException;
import com.truelayer.pokedex.model.PokemonInfo;
import com.truelayer.pokedex.model.PokemonSpecies;
import com.truelayer.pokedex.service.PokemonServiceImpl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;


import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PokemonController.class)
public class PokemonControllerTest {

    @MockBean
    private PokemonServiceImpl service;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("Should return Pokeman Basic information")
    public void getPokemonBasicInfo() throws Exception {
        Mockito.when(service.pokemonInfo("ssr")).thenReturn(getPokemonInfo("ssr","cave","this is awesome",false,null));
        mockMvc.perform(get("/{pokemon}","ssr")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("ssr")));

    }

    @Test
    @DisplayName("Should return exception for invalid pokemon")
    public void getPokemonBasicInfoNotFound() throws Exception {
        Mockito.when(service.pokemonInfo("ssr")).thenThrow(new PokemonException("no data found"));

        Exception exception = assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(get("/{pokemon}","ssr")).andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is("ssr")));
        });

        String expectedMessage = "no data found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Should return NOT FOUND  when response is null")
    public void getPokemonBasicInfoNotFoundWhenResponseIsNullFromService() throws Exception {
        Mockito.when(service.pokemonInfo("ssr")).thenReturn(null);
            mockMvc.perform(get("/{pokemon}","ssr")).andDo(print())
                    .andExpect(status().is(404));

    }

    @Test
    @DisplayName("Should return Pokeman translated information")
    public void getPokemonTranslatedInfo() throws Exception {
        Mockito.when(service.getPokemonTranslatedInfo("ssr")).thenReturn(getPokemonInfo("ssr","cave","this is awesome",false,"yoda translated text"));
        mockMvc.perform(get("/translated/{pokemon}","ssr")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("ssr")))
                .andExpect(jsonPath("$.habitat", is("cave")))
                .andExpect(jsonPath("$.description", is("yoda translated text")))
                .andExpect(jsonPath("$.is_legendary", is(false)))
        ;

    }

    @Test
    @DisplayName("Should return exception for invalid pokemon for getting translated info")
    public void getPokemonTranslatedInfoNotFound() throws Exception {
        Mockito.when(service.getPokemonTranslatedInfo("ssr")).thenThrow(new PokemonException("no data found"));

        Exception exception = assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(get("/translated/{pokemon}","ssr")).andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is("ssr")));
        });

        String expectedMessage = "no data found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }


    private PokemonInfo getPokemonInfo(String name,String habitat,String flavourText,boolean isLegandary,String translatedText){
        PokemonSpecies species = new PokemonSpecies();
        PokemonSpecies.Habitant habitant = new PokemonSpecies.Habitant();
        habitant.setName(habitat);
        PokemonSpecies.FlavorTextEntry flavour = new PokemonSpecies.FlavorTextEntry();
        flavour.setFlavorText(flavourText);
        flavour.setTranslatedText(translatedText);
        species.setHabitat(habitant);
        species.setLegendary(isLegandary);
        species.setFlavorTextEntries(Arrays.asList(flavour));
        PokemonInfo info =  PokemonInfo.builder().pokemonSpecies(species).build();
        info.setName(name);
        return info;
    }
}
