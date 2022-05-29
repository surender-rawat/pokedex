package com.truelayer.pokedex.exceptions;

public class PokemonException extends RuntimeException {
    public PokemonException(String message) {
        super(message);
    }

    public PokemonException(String message, Throwable cause) {
        super(message, cause);
    }
}

