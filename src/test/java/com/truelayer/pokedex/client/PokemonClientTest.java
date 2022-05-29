package com.truelayer.pokedex.client;


import com.truelayer.pokedex.exceptions.PokemonException;
import com.truelayer.pokedex.model.PokemonSpecies;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;


import java.util.Arrays;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;


@ExtendWith(MockitoExtension.class)
public class PokemonClientTest {

    @InjectMocks
    PokemonClient client;

    @Mock
    RestTemplate restTemplate;

    @Mock
    ResponseEntity<Object> responseEntity;

    @Mock
    Environment env;

    @Test
    @DisplayName("Should return Pokeman Basic information")
    public void shouldGetPokemonInfo(){
        Mockito.when(env.getProperty("pokedex.api.poke_api_url")).thenReturn("http://localhost:9000");
        Mockito.when(restTemplate.getForEntity(anyString(),any())).thenReturn(responseEntity);
        Mockito.when(responseEntity.getStatusCodeValue()).thenReturn(200);
        Mockito.when(responseEntity.getBody()).thenReturn(getPokemonSpecies("ssr","forest", false));
        PokemonSpecies pokemonSpecies = client.getPokemonSpecies("ssr");
        assertTrue(pokemonSpecies.getName().equals("ssr"));
        assertTrue(pokemonSpecies.getHabitat().getName().equals("forest"));
        assertTrue(!pokemonSpecies.isLegendary());
    }

    @Test
    @DisplayName("Should return exception for invalid pokemon")
    public void shouldGetExceptionForInvalidPokemon(){
        Mockito.when(env.getProperty("pokedex.api.poke_api_url")).thenReturn("http://localhost:9000");
        Mockito.when(restTemplate.getForEntity(anyString(),any())).thenThrow(new ResourceAccessException("exception"));
        Exception exception = assertThrows(PokemonException.class, () -> {
            PokemonSpecies pokemonSpecies = client.getPokemonSpecies("ssr");
        });

        String expectedMessage = "error while acessing api";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    private PokemonSpecies getPokemonSpecies(String name, String habitat, boolean isLegeandry){
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
