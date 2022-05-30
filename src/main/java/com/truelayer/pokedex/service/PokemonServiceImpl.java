package com.truelayer.pokedex.service;


import com.truelayer.pokedex.client.PokemonClient;
import com.truelayer.pokedex.client.TextTranslatorClient;
import com.truelayer.pokedex.model.PokemonInfo;
import com.truelayer.pokedex.model.PokemonSpecies;
import com.truelayer.pokedex.model.Translator;
import com.truelayer.pokedex.util.PokemonUtility;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Pokemon Service implementation class
 */
@Slf4j
@Service
public class PokemonServiceImpl implements PokemonService {

    private static final String CAVE = "cave";

    private final PokemonClient client;
    private final TextTranslatorClient translatorClient;

    @Autowired
    public PokemonServiceImpl(PokemonClient client, TextTranslatorClient translatorClient) {
        this.client = client;
        this.translatorClient = translatorClient;
    }

    /**
     * This method gets the pokemon basic information
     *
     * @param name
     * @return
     */
    public PokemonInfo pokemonInfo(String name) {
        PokemonSpecies pokemonSpecies = client.getPokemonSpecies(name);
        PokemonInfo info = PokemonInfo.builder().pokemonSpecies(pokemonSpecies).build();
        info.setName(name);
        return info;
    }

    public PokemonInfo getPokemonTranslatedInfo(String name) {
        PokemonInfo info = pokemonInfo(name);
        if (null != info && null != info.getPokemonSpecies()) {
            String habitat = info.getPokemonSpecies().getHabitat().getName();
            boolean isLegendary = info.getPokemonSpecies().isLegendary();
            String description = PokemonUtility.description.apply(info);
            String translatedText = null;
            if (StringUtils.equals(habitat, CAVE) || isLegendary)
                translatedText = translatorClient.getTranslatorText(description, Translator.YODA);
            else translatedText = translatorClient.getTranslatorText(description, Translator.SHAKESPEARE);

            if (!org.springframework.util.StringUtils.isEmpty(translatedText)) {
                PokemonUtility.addTranslatedDescription.apply(info, translatedText);
            } else {
                PokemonUtility.addTranslatedDescription.apply(info, description);
            }
        }
        return info;
    }

}
