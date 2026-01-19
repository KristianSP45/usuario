package com.kristian.usuario.controller;

import com.kristian.usuario.infrastructure.exceptions.ConflictException;
import com.kristian.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.kristian.usuario.infrastructure.exceptions.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice//Pense assim: É um “controller invisível” só para erros
public class GlobalExceptionHandler {//“O GlobalExceptionHandler centraliza o tratamento de exceções da aplicação,
    // garantindo respostas HTTP consistentes e organizadas.”

    @ExceptionHandler(ResourceNotFoundException.class)
    //Significa literalmente:“Quando essa exceção acontecer, usa ESTE método para responder”
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex){
        return  new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<String> handleConflictException(ConflictException ex){
        return  new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException ex){
        return  new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
