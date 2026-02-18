package com.kristian.usuario.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Service//Diz ao Spring: “Essa classe pode ser injetada em outras”
// obs: Poderia ser @Component, mas é melhor esse @...
@Component
public class JwtUtil {// JwtUtil é responsável por gerar, extrair e validar tokens JWT,
// sendo usado no processo de autenticação via Spring Security

    // Chave secreta usada para assinar e verificar tokens JWT
    @Value("${jwt.secret}")
    private String secretKey;
    //Essa é a chave codificada em Base64, é só uma forma de representar bytes como texto.
    //Pensa assim: Quem tem essa chave > consegue criar token
    //Quem não tem > não consegue falsificar

    //Site para decodificar e codificar: https://www.base64encode.org/pt/
    private SecretKey getSecretKey(){
        byte[] key = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(key);
        //1️ Você pega a string Base64
        //2️ Decodifica ela > vira array de bytes
        //3️ O Keys.hmacShaKeyFor(key) transforma esses bytes numa SecretKey válida para HMAC
    }

    // Gera um token JWT com o nome de usuário e validade de 1 hora
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username) // Define o email de usuário como o assunto do token
                .issuedAt(new Date()) // Define a data e hora de emissão do token
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60)) // Define a data e hora de expiração (1 minuto a partir da emissão)
                .signWith(getSecretKey()) // Converte a chave secreta em bytes e assina o token com ela
                .compact(); // Constrói o token JWT
    }
    // Extrai as claims do token JWT (informações adicionais do token)
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey()) // Define a chave secreta para validar a assinatura do token
                .build()
                .parseSignedClaims(token) // Analisa o token JWT e obtém as claims
                .getPayload();  // Obtém o payload (corpo) do token, que contém as claims
    }

    // Extrai o nome de usuário do token JWT
    public String extractUsername(String token) {
        // Obtém o assunto (nome de usuário) das claims do token
        return extractClaims(token).getSubject();
    }

    // Verifica se o token JWT está expirado
    public boolean isTokenExpired(String token) {
        // Compara a data de expiração do token com a data atual
        return extractClaims(token).getExpiration().before(new Date());
    }

    // Valida o token JWT verificando o nome de usuário e se o token não está expirado
    public boolean validateToken(String token, String username) {
        // Extrai o nome de usuário do token
        final String extractedUsername = extractUsername(token);
        // Verifica se o nome de usuário do token corresponde ao fornecido e se o token não está expirado
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
}
/*
Antes do metodo SecretKey o codigo era:

    // Gera um token JWT com o nome de usuário e validade de 1 hora
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // Define o nome de usuário como o assunto do token
                .setIssuedAt(new Date()) // Define a data e hora de emissão do token

                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60))
                // Define a data e hora de expiração (1 hora a partir da emissão, 1000 * 60 * 60 sendo 1 hora, agora está 1 minuto)

                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                // Converte a chave secreta em bytes e assina o token com ela, impossibilitando falsificação

                .compact(); // Constrói o token JWT
    }

    // Extrai as claims do token JWT (informações adicionais do token)
    public Claims extractClaims(String token) {
        return Jwts.parser()

                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                // Define a chave secreta para validar a assinatura do token

                .build()
                .parseClaimsJws(token) // Analisa o token JWT e obtém as claims
                .getBody(); // Retorna o corpo das claims
    }
 */