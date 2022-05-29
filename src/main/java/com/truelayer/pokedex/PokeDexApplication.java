package com.truelayer.pokedex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class PokeDexApplication {

    public static void main(String[] args) {
        SpringApplication.run(PokeDexApplication.class,args);
    }

}
