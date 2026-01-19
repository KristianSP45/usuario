package com.kristian.usuario.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Define a classe JwtRequestFilter, que estende OncePerRequestFilter
public class JwtRequestFilter extends OncePerRequestFilter {
    //JwtRequestFilter = Um porteiro que fica na porta da API, olhando TODA requisição
    //e decidindo se o usuário está autenticado ou não.
    //extends OncePerRequestFilter = Esse código roda toda vez que alguém faz uma requisição na API

    // Define propriedades para armazenar instâncias de JwtUtil e UserDetailsService
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    // Construtor que inicializa as propriedades com instâncias fornecidas
    public JwtRequestFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    // Método chamado uma vez por requisição para processar o filtro
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Obtém o valor do header "Authorization" da requisição
        final String authorizationHeader = request.getHeader("Authorization");

        // Verifica se o cabeçalho existe e começa com "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Extrai o token JWT do cabeçalho
            final String token = authorizationHeader.substring(7);
            // Extrai o nome de usuário do token JWT
            final String username = jwtUtil.extractUsername(token);

            // Se o nome de usuário não for nulo e o usuário não estiver autenticado ainda
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Carrega os detalhes do usuário a partir do nome de usuário
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                // Valida o token JWT
                if (jwtUtil.validateToken(token, username)) {
                    // Cria um objeto de autenticação com as informações do usuário
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    // Define a autenticação no contexto de segurança
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        // Continua a cadeia de filtros, permitindo que a requisição prossiga
        chain.doFilter(request, response);
    }
}
// Filtro responsável por interceptar requisições,
// validar o JWT e autenticar o usuário no contexto do Spring Security