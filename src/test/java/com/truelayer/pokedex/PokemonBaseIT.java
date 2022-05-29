package com.truelayer.pokedex;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PokemonBaseIT {

    @Autowired
    private TestRestTemplate restTemplate;
    protected static String getStringFromFile(String fileName) throws Exception {
        URL resource = PokemonBaseIT.class.getClassLoader().getResource(fileName);
        byte[] bytes = Files.readAllBytes(Paths.get(resource.toURI()));
        return new String(bytes);
    }

    protected TestRestTemplate getRestTemplate()
    {
        TestRestTemplate template = new TestRestTemplate();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        template.getRestTemplate().setMessageConverters (messageConverters);
        return restTemplate;
    }
}
