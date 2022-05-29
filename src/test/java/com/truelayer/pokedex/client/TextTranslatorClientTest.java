package com.truelayer.pokedex.client;

import com.truelayer.pokedex.model.Translator;
import com.truelayer.pokedex.model.TranslatorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class TextTranslatorClientTest {
    @InjectMocks
    TextTranslatorClient client;

    @Mock
    RestTemplate restTemplate;

    @Mock
    ResponseEntity<Object> responseEntity;

    @Mock
    Environment env;

    @Test
    @DisplayName("Should return translated text")
    public void shouldGetTranslatedText(){
        Mockito.when(restTemplate.postForEntity(anyString(),any(),any())).thenReturn(responseEntity);
        Mockito.when(responseEntity.getStatusCodeValue()).thenReturn(200);
        Mockito.when(responseEntity.getBody()).thenReturn(translatorResponse(Translator.YODA));
        String text = client.getTranslatorText("test",Translator.YODA);
        assertTrue(text.contains("TRANSLATED TEXT YODA"));
    }


    private TranslatorResponse translatorResponse(Translator translator){
        TranslatorResponse res = new TranslatorResponse();
        TranslatorResponse.Contents contents = new TranslatorResponse.Contents();
        String text = (translator == Translator.YODA)? "TRANSLATED TEXT YODA" : "TRANSLATED TEXT SHAKESPEARE";
        contents.setTranslated(text);
        res.setContents(contents);
        return res;
    }

}
