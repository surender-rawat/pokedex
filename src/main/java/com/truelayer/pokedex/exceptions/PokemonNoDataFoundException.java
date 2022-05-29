package com.truelayer.pokedex.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PokemonNoDataFoundException extends RuntimeException{
    public PokemonNoDataFoundException(String message) {
        super(message);
    }

    public PokemonNoDataFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
