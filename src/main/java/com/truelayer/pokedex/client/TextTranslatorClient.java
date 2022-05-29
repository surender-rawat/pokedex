package com.truelayer.pokedex.client;

import com.truelayer.pokedex.exceptions.PokemonException;
import com.truelayer.pokedex.model.Translator;
import com.truelayer.pokedex.model.TranslatorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class TextTranslatorClient {

    private static final String SHAKESPEAR_TRANSLATOR = "/translate/shakespeare.json";
    private static final  String YODA_TRANSLATOR = "/translate/yoda.json";

    private static final String TRANSLATOR_API = "pokedex.api.translator_api_url";
    private RestTemplate restTemplate;
    private Environment env;

    @Autowired
    public TextTranslatorClient(RestTemplate restTemplate, Environment env) {
        this.restTemplate = restTemplate;
        this.env = env;
    }

    @Retryable(maxAttemptsExpression = "#{${pokedex.resttemplate.retry.count}}", value = PokemonException.class,
            backoff = @Backoff(delayExpression = "#{${pokedex.resttemplate.backoffpolicy.delay.millisec}}", multiplierExpression = "#{${pokedex.resttemplate.backoffpolicy.multiplier}}"))
    public String getTranslatorText(String input, Translator translator) {
        String translatedText = null;
        try {
            TranslatorResponse translatorResponse = null;
            String apiUrl = env.getProperty(TRANSLATOR_API)+((translator==Translator.SHAKESPEARE)? SHAKESPEAR_TRANSLATOR: YODA_TRANSLATOR) ;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
            map.add("text", input);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            ResponseEntity<TranslatorResponse> response = restTemplate.postForEntity(apiUrl,request, TranslatorResponse.class);
            if (response.getStatusCodeValue() == 200) {
                translatorResponse = response.getBody();
                translatedText = translatorResponse.getContents().getTranslated();
            }
        } catch (ResourceAccessException exp) {
            log.error("error while accessing the api - {}", exp);
        }
        return translatedText;
    }

}
