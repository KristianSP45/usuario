package com.kristian.usuario.infrastructure.exceptions;

public class ResourceNotFoundException extends RuntimeException{//extends RuntimeException = exceção não verificada
    public ResourceNotFoundException(String mensagem){
        super(mensagem);
    }

    public ResourceNotFoundException(String mensagem, Throwable throwable){
        super(mensagem, throwable);
    }
}
