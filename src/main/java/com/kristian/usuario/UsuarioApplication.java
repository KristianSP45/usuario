package com.kristian.usuario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class UsuarioApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsuarioApplication.class, args);
	}

}
//Só JwtUtil e UserDetailsServiceImpl usam @Service?
//✔ Correto.
//JwtRequestFilter → é filtro, não service
//SecurityConfig → é configuração (@Configuration)
//UserDetailsServiceImpl → regra de negócio de auth