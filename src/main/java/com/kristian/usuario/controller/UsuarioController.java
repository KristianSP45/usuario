package com.kristian.usuario.controller;

import com.kristian.usuario.business.UsuarioService;
import com.kristian.usuario.business.ViaCepService;
import com.kristian.usuario.business.dto.EnderecoDTO;
import com.kristian.usuario.business.dto.TelefoneDTO;
import com.kristian.usuario.business.dto.UsuarioDTO;
import com.kristian.usuario.infrastructure.clients.ViaCepDTO;
import com.kristian.usuario.infrastructure.security.JwtUtil;
import com.kristian.usuario.infrastructure.security.SecurityConfig;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController //diz que é controller E diz que responde JSON
@RequestMapping("/usuario")//Prefixo da API, todos os endpoints começam com: /usuario
@RequiredArgsConstructor //@RequiredArgsConstructor = “Spring, usa esse construtor pra injetar”
@Tag(name = "Usuarios", description = "Cadastra usuários")
@SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME)
public class UsuarioController {

    private final UsuarioService usuarioService;//“cria um construtor com final, que seria fixo,
    // e traz o UsuarioService para esse construtor”
    private final AuthenticationManager authenticationManager;//final = “isso é obrigatório”
    private final JwtUtil jwtUtil;
    private final ViaCepService viaCepService;

    @PostMapping//Não somente esse,mas a informação se aplica aos outros verbos HTTP.
    // Ele informa DUAS coisas: verbo HTTP = GET E rota = /teste, os dois juntos = @GetMapping("/teste")
    public ResponseEntity<UsuarioDTO> salvarUsuario(@RequestBody UsuarioDTO usuarioDTO){// ResponseEntity<> =
        // dizer se deu certo, dizer se deu erro E controlar resposta HTTP
        return ResponseEntity.ok(usuarioService.salvarUsuario(usuarioDTO));// ResponseEntity.ok(dado) =
        // status: 200 OK E corpo: dado - “Deu certo e aqui está o resultado”
    }

    @PostMapping("/login")
    public String login(@RequestBody UsuarioDTO usuarioDTO){//@RequestBody = corpo da requisição
        // “Spring, o corpo da requisição vem em JSON, transforma isso em UsuarioDTO.”
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuarioDTO.getEmail(),usuarioDTO.getSenha())
        );
        return "Bearer "+jwtUtil.generateToken(authentication.getName());
    }

    @GetMapping
    public  ResponseEntity<UsuarioDTO> buscarUsuarioPorEmail(@RequestParam("email") String email){
        //@RequestParam("email") = “Spring, pega o parâmetro email da requisição e coloca aqui nessa variável.”
        //Esse parâmetro fica na URL, depois do ?. Use quando: filtro, busca, opcional
        return  ResponseEntity.ok(usuarioService.buscarUsuarioPorEmail(email));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deletarUsuarioPorEmail(@PathVariable String email){//@PathVariable + /{email} =
        // O {email} vira variável. Use quando: o dado identifica o recurso faz parte do caminho (URL)
        usuarioService.deletaUsuarioPorEmail(email);
        return  ResponseEntity.ok().build();// ResponseEntity.ok().build() =
        // status: 200 OK E corpo: vazio - “Deu certo, mas não tem nada pra devolver”
    }

    @PutMapping
    public ResponseEntity<UsuarioDTO> atualizarDadoUsuario(@RequestBody UsuarioDTO dto,
                                                           @RequestHeader("Authorization") String token){
        //Obs: Header é qualquer metadado da requisição. Exemplos: Authorization, Content-Type, Accept
        //@RequestHeader("Authorization") = Aqui está pegando apenas o header Authorization, que é de segurança (JWT).
        return ResponseEntity.ok(usuarioService.atualizaDadosUsuario(token, dto));
    }

    @PutMapping("/endereco")
    public ResponseEntity<EnderecoDTO> atualizaEndereco(@RequestBody EnderecoDTO dto,
                                                        @RequestParam("id") Long id){
        return  ResponseEntity.ok(usuarioService.atualizaEndereco(id, dto));
    }

    @PutMapping("/telefone")
    public ResponseEntity<TelefoneDTO> atualizaTelefone(@RequestBody TelefoneDTO dto,
                                                        @RequestParam("id") Long id){
        return  ResponseEntity.ok(usuarioService.atualizaTelefone(id, dto));
    }

    @PostMapping("/endereco")
    public  ResponseEntity<EnderecoDTO> cadastraEndereco(@RequestBody EnderecoDTO dto,
                                                         @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(usuarioService.cadastraEndereco(token, dto));
    }

    @PostMapping("/telefone")
    public  ResponseEntity<TelefoneDTO> cadastraTelefone(@RequestBody TelefoneDTO dto,
                                                         @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(usuarioService.cadastraTelefone(token, dto));
    }

    @GetMapping("/endereco/{cep}")
    public  ResponseEntity<ViaCepDTO> buscarDadosCep(@PathVariable("cep") String cep){
        return ResponseEntity.ok(viaCepService.buscarDadosEndereco(cep));
    }
}
