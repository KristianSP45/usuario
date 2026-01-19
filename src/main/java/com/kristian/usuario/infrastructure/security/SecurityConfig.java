package com.kristian.usuario.infrastructure.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration//Diz ao Spring: “Essa classe contém configurações da aplicação”
@EnableWebSecurity//Ativa o Spring Security
public class SecurityConfig {// Classe responsável por configurar o Spring Security,
// definir regras de acesso, autenticação via JWT
// e registrar o filtro de segurança da aplicação

    // Instâncias de JwtUtil e UserDetailsService injetadas pelo Spring
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    // Construtor para injeção de dependências de JwtUtil e UserDetailsService
    @Autowired//Construtor manual, mas sem o construtor era só usar o @RequiredArgsConstructor e apagar o construtor abaixo
    public SecurityConfig(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    // Configuração do filtro de segurança
    @Bean//é usado para salvar um retorno que não é de uma classe do projeto, para o Spring saber criar a injeção.
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Cria uma instância do JwtRequestFilter com JwtUtil e UserDetailsService
        JwtRequestFilter jwtRequestFilter = new JwtRequestFilter(jwtUtil, userDetailsService);

        http
                .csrf(AbstractHttpConfigurer::disable) // Desativa proteção CSRF para APIs REST (não aplicável a APIs que não mantêm estado)
                .authorizeHttpRequests(authorize -> authorize//Aqui você define quem pode acessar o quê.
                        .requestMatchers("/usuario/login").permitAll() // Permite acesso ao endpoint de login sem autenticação
                        .requestMatchers(HttpMethod.GET, "/auth").permitAll()// Permite acesso ao endpoint GET /auth sem autenticação
                        .requestMatchers(HttpMethod.POST, "/usuario").permitAll() // Permite acesso ao endpoint POST /usuario sem autenticação
                        .requestMatchers("/usuario/**").authenticated() // Requer autenticação para qualquer endpoint que comece com /usuario/ obs:tem que ser depois dos requestMatchers que não precisam de auth
                        .anyRequest().authenticated() // Requer autenticação para todas as outras requisições
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Configura a política de sessão como stateless (sem sessão)
                        //JWT = stateless
                        //Sessão = stateful
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // Adiciona o filtro JWT antes do filtro de autenticação padrão

        // Retorna a configuração do filtro de segurança construída
        return http.build();
    }

    // Configura o PasswordEncoder para criptografar senhas usando BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Retorna uma instância de BCryptPasswordEncoder
        //Usado aqui: passwordEncoder.encode(...)
    }

    // Configura o AuthenticationManager usando AuthenticationConfiguration
    @Bean
    //AuthenticationManager valida as credenciais usando os dados carregados pelo UserDetailsService e o PasswordEncoder.
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {//Ele: valida usuário, compara senha, usa UserDetailsService, usa PasswordEncoder
        // Obtém e retorna o AuthenticationManager da configuração de autenticação
        return authenticationConfiguration.getAuthenticationManager();
        //Usado no login: authenticationManager.authenticate(...)
    }

}
