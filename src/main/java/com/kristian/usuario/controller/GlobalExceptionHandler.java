package com.kristian.usuario.controller;

import com.kristian.usuario.infrastructure.exceptions.ConflictException;
import com.kristian.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.kristian.usuario.infrastructure.exceptions.UnauthorizedException;
import com.kristian.usuario.infrastructure.exceptions.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice//Pense assim: É um “controller invisível” só para erros
public class GlobalExceptionHandler {//“O GlobalExceptionHandler centraliza o tratamento de exceções da aplicação,
    // garantindo respostas HTTP consistentes e organizadas.”

    @ExceptionHandler(ResourceNotFoundException.class)
    //Significa literalmente:“Quando essa exceção acontecer, usa ESTE método para responder”
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request){
        //ResponseEntity<ErrorResponseDTO> = Retorna um objeto estruturado
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildError(HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getRequestURI(),
                "Not Found"
                ));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<String> handleConflictException(ConflictException ex){
        //ResponseEntity<String> = Retorna só texto simples.
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Cria o objeto de erro padrão da API.
    // Aqui NÃO precisamos converter para JSON,
    // porque o Spring faz isso automaticamente.
    private ErrorResponseDTO buildError(int status, String mensagem, String path, String error){//buildError = No Global → ele retorna DTO
        return ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .message(mensagem)
                .status(status)
                .path(path)
                .error(error)
                .build();
        //E o Spring usa o ObjectMapper interno dele.
        //Você NÃO precisa criar ObjectMapper.
    }
}
