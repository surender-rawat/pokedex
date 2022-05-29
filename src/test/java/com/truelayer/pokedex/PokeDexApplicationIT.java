package com.truelayer.pokedex;


import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.truelayer.pokedex.model.response.PokemonDetailResponse;

import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PokeDexApplicationIT extends PokemonBaseIT {

    @LocalServerPort
    private int port;


    @RegisterExtension
    static WireMockExtension wm1 = WireMockExtension.newInstance()
            .options(wireMockConfig().port(9000))
            .build();


    @BeforeEach
    private void setup() throws Exception {
        wm1.stubFor(com.github.tomakehurst.wiremock.client.WireMock.get(urlPathEqualTo("/api/v2/pokemon-species/onix"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(getStringFromFile("onix.json"))
                        .withHeader("Content-Type", "application/json")));
        wm1.stubFor(com.github.tomakehurst.wiremock.client.WireMock.get(urlPathEqualTo("/api/v2/pokemon-species/metapod"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(getStringFromFile("metapod.json"))
                        .withHeader("Content-Type", "application/json")));
        wm1.stubFor(com.github.tomakehurst.wiremock.client.WireMock.get(urlPathEqualTo("/api/v2/pokemon-species/kakuna"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(getStringFromFile("kakuna.json"))
                        .withHeader("Content-Type", "application/json")));

        wm1.stubFor(com.github.tomakehurst.wiremock.client.WireMock.post(urlPathEqualTo("/translate/yoda.json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(getStringFromFile("yoda.json"))
                        .withHeader("Content-Type", "application/json")));

        wm1.stubFor(com.github.tomakehurst.wiremock.client.WireMock.post(urlPathEqualTo("/translate/shakespeare.json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(getStringFromFile("shakespeare.json"))
                        .withHeader("Content-Type", "application/json")));
    }

    @Test
    @DisplayName("Should return Pokeman Basic information")
    public void shouldGetResponseFromPokemonServiceForValidPokemonInfo() throws Exception {
        ResponseEntity<PokemonDetailResponse> responseEntity = getRestTemplate().getForEntity(
                createURLWithPort("/pokemon/onix"),
                PokemonDetailResponse.class);
        PokemonDetailResponse response = responseEntity.getBody();
        Assertions.assertThat(response.getName().equals("onix")).isTrue();
        assertThat(response.getHabitat().equals("cave")).isTrue();
        assertThat(response.isLegendary()).isFalse();
    }

    @Test
    @DisplayName("Should return Pokeman translated information for habitat as cave ")
    public void shouldGetResponseForPokemonTranslatorServiceForYodaAsHibitatIsCave() throws Exception {
        ResponseEntity<PokemonDetailResponse> responseEntity = getRestTemplate().getForEntity(
                createURLWithPort("/pokemon/translated/onix"),
                PokemonDetailResponse.class);
        PokemonDetailResponse response = responseEntity.getBody();
        Assertions.assertThat(response.getName().equals("onix")).isTrue();
        assertThat(response.getHabitat().equals("cave")).isTrue();
        assertThat(response.isLegendary()).isFalse();
        assertThat(response.getDescription().equals("yoda translation")).isTrue();
    }

    @Test
    @DisplayName("Should return Pokeman translated information for habitat as forest ")
    public void shouldGetResponseForPokemonTranslatorServiceForShakespeareAsHibitatIsForest() throws Exception {
        ResponseEntity<PokemonDetailResponse> responseEntity = getRestTemplate().getForEntity(
                createURLWithPort("/pokemon/translated/metapod"),
                PokemonDetailResponse.class);
        PokemonDetailResponse response = responseEntity.getBody();
        Assertions.assertThat(response.getName().equals("metapod")).isTrue();
        assertThat(response.getHabitat().equals("forest")).isTrue();
        assertThat(response.isLegendary()).isFalse();
        assertThat(response.getDescription().equals("shakespeare translation")).isTrue();
    }

    @Test
    @DisplayName("Should return Pokeman translated information for legendary ")
    public void shouldGetResponseForPokemonTranslatorServiceForYodaAsHibitatIsForestButLegendary() throws Exception {
        ResponseEntity<PokemonDetailResponse> responseEntity = getRestTemplate().getForEntity(
                createURLWithPort("/pokemon/translated/kakuna"),
                PokemonDetailResponse.class);
        PokemonDetailResponse response = responseEntity.getBody();
        Assertions.assertThat(response.getName().equals("metapod")).isFalse();
        assertThat(response.getHabitat().equals("forest")).isTrue();
        assertThat(response.isLegendary()).isTrue();
        assertThat(response.getDescription().equals("yoda translation")).isTrue();
    }

    @Test
    @DisplayName("Should return NO INFO FOUND  for invalid pokemon ")
    public void shouldGetNotFoundForwrongPokemonFromPokemonService() throws Exception {
        wm1.stubFor(com.github.tomakehurst.wiremock.client.WireMock.get(urlPathEqualTo("/api/v2/pokemon-species/onix123"))
                .willReturn(status(404)));


        ResponseEntity<PokemonDetailResponse> responseEntity = getRestTemplate().getForEntity(
                createURLWithPort("/pokemon/onix123"),
                PokemonDetailResponse.class);
        assertThat(responseEntity.getStatusCode()==HttpStatus.NOT_FOUND).isTrue();
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}