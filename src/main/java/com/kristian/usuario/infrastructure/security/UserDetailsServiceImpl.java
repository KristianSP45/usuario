package com.kristian.usuario.infrastructure.security;

import com.kristian.usuario.infrastructure.entity.Usuario;
import com.kristian.usuario.infrastructure.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    //implements UserDetailsService = é responsável APENAS por buscar o usuário
    //Essa classe é a ponte entre o Spring Security e o seu banco de dados.

    // Repositório para acessar dados de usuário no banco de dados
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Implementação do método para carregar detalhes do usuário pelo e-mail
    @Override
    //Função ÚNICA loadUserByUsername: Dado um username (email), devolver os dados do usuário(username,senha,roles)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Busca o usuário no banco de dados pelo e-mail
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        // Cria e retorna um objeto UserDetails com base no usuário encontrado
        return org.springframework.security.core.userdetails.User
                .withUsername(usuario.getEmail()) // Define o nome de usuário como o e-mail
                .password(usuario.getSenha()) // Define a senha do usuário
                .build(); // Constrói o objeto UserDetails
        //OU return usuario;
    }
}
