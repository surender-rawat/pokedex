package com.truelayer.pokedex.exceptions.handler;

import com.truelayer.pokedex.exceptions.PokemonNoDataFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * This is controller advice to handle all exceptions
 */
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = PokemonNoDataFoundException.class)
    public ResponseEntity<Object> exception(PokemonNoDataFoundException exception) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
