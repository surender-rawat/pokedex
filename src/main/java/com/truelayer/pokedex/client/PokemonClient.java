package com.truelayer.pokedex.client;

import com.truelayer.pokedex.exceptions.PokemonException;
import com.truelayer.pokedex.model.PokemonSpecies;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class PokemonClient {

    private RestTemplate restTemplate;
    private Environment env;

    private static final String POKEMON_SPICES = "/api/v2/pokemon-species/";
    private static final String POKEMON_API= "pokedex.api.poke_api_url";

    @Autowired
    public PokemonClient( Environment env,RestTemplate restTemplate) {
        this.env = env;
        this.restTemplate = restTemplate;
    }

    @Retryable(maxAttemptsExpression = "#{${pokedex.resttemplate.retry.count}}", value = PokemonException.class,
            backoff = @Backoff(delayExpression = "#{${pokedex.resttemplate.backoffpolicy.delay.millisec}}", multiplierExpression = "#{${pokedex.resttemplate.backoffpolicy.multiplier}}"))
    public PokemonSpecies getPokemonSpecies(String name) {
        PokemonSpecies basicInfo = null;
        log.info("getting for the pokemon basic info for -  {}", name);
        try {
            ResponseEntity<PokemonSpecies> response = restTemplate.getForEntity(env.getProperty(POKEMON_API)+POKEMON_SPICES + name, PokemonSpecies.class);
            if (response.getStatusCodeValue() != 200)
                throw new PokemonException("error while acessing api ");
            basicInfo =  response.getBody();
        } catch (ResourceAccessException exp) {
            log.error("error while accessing the api - {}", exp);
            throw new PokemonException("error while acessing api " + exp);
        }
            return basicInfo;
    }




}
