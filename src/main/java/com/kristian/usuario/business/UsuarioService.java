package com.kristian.usuario.business;

import com.kristian.usuario.business.dto.EnderecoDTO;
import com.kristian.usuario.business.dto.TelefoneDTO;
import com.kristian.usuario.business.dto.UsuarioDTO;
import com.kristian.usuario.business.converter.UsuarioConverter;
import com.kristian.usuario.infrastructure.entity.Endereco;
import com.kristian.usuario.infrastructure.entity.Telefone;
import com.kristian.usuario.infrastructure.entity.Usuario;
import com.kristian.usuario.infrastructure.exceptions.ConflictException;
import com.kristian.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.kristian.usuario.infrastructure.exceptions.UnauthorizedException;
import com.kristian.usuario.infrastructure.repository.EnderecoRepository;
import com.kristian.usuario.infrastructure.repository.TelefoneRepository;
import com.kristian.usuario.infrastructure.repository.UsuarioRepository;
import com.kristian.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service// diz ao Spring: “essa classe tem regra de negócio”
@RequiredArgsConstructor// gera o construtor com os final
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private static final String ERROR_PREFIX = "Email não encontrado ";

    public UsuarioDTO salvarUsuario(UsuarioDTO usuarioDTO){
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        //passwordEncoder.encode = Criptografa a senha
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        //Sem Converter (o caos)
        //Usuario usuario = new Usuario();
        //usuario.setNome(dto.getNome());
        //usuario.setEmail(dto.getEmail());
        //usuario.setSenha(dto.getSenha());
        //
        //Com Converter (o certo)
        //Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        //“Converter, transforma esse DTO em Entity pra mim.”


        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));

        //OU  usuario = usuarioRepository.save(usuario);
        //return usuarioConverter.paraUsuarioDTO(usuario);
    }

    public String autenticarUsuario(UsuarioDTO usuarioDTO){
        try {
            Authentication authentication = authenticationManager.authenticate(//Esse cara é do Spring Security
                    new UsernamePasswordAuthenticationToken(usuarioDTO.getEmail(),usuarioDTO.getSenha())
            );
            return "Bearer "+jwtUtil.generateToken(authentication.getName());
        } catch (BadCredentialsException | UsernameNotFoundException | AuthorizationDeniedException e){
            throw new UnauthorizedException("Usuário ou senha inválidos: ",e.getCause());
        }
    }

    public void emailExiste(String email){
        try {
            boolean existe = verificaEmailExistente(email);
            if (existe){
                throw new ConflictException("Email já cadastrado"+email);
            }
        } catch (ConflictException e){
            throw new ConflictException("Email já cadastrado"+e.getCause());
        }
    }

    public boolean verificaEmailExistente(String email){

        return usuarioRepository.existsByEmail(email);
    }

    public  UsuarioDTO buscarUsuarioPorEmail(String email){
        try {
            return usuarioConverter.paraUsuarioDTO(usuarioRepository.findByEmail(email).orElseThrow(
                    () -> new ResourceNotFoundException(ERROR_PREFIX+email)));

        } catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException(ERROR_PREFIX+email);
        }
    }

    public void deletaUsuarioPorEmail(String email){
        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO dto){
        //Aqui buscamos o email do usuario atraves do token (tirar a obrigatoriedade do email)
        String email = jwtUtil.extractUsername(token.substring(7));// Extrai email do token.
        // token.substring(7) = Ele ignora os primeiros 7 caracteres da string em java

        //Criptografia de senha
        dto.setSenha(dto.getSenha() !=null ? passwordEncoder.encode(dto.getSenha()) : null );

        //Busca os dados do usuario no banco de dados
        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException(ERROR_PREFIX));

        //Mesclou os dados que recebemos na requisição DTO com os dados do banco de dados
        Usuario usuario = usuarioConverter.updateUsuario(dto, usuarioEntity);

        //Salvou os dados do usuario convertido e depois pegou o retorno e converteu para usuarioDTO
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public EnderecoDTO atualizaEndereco(Long idEndereco, EnderecoDTO enderecoDTO){
        Endereco entity = enderecoRepository.findById(idEndereco).orElseThrow(
                () -> new ResourceNotFoundException("Id não encontrado"+idEndereco));
        Endereco endereco = usuarioConverter.updateEndereco(enderecoDTO, entity);
        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));
    }

    public TelefoneDTO atualizaTelefone(Long idTelefone, TelefoneDTO dto){
        Telefone entity = telefoneRepository.findById(idTelefone).orElseThrow(
                () -> new ResourceNotFoundException("Id não encontrado"+idTelefone));
        Telefone telefone = usuarioConverter.updateTelefone(dto, entity);
        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));
    }

    public EnderecoDTO cadastraEndereco(String token, EnderecoDTO dto){
        String email = jwtUtil.extractUsername(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException(ERROR_PREFIX));
        Endereco endereco = usuarioConverter.paraEnderecoEntity(dto, usuario.getId());
        Endereco enderecoEntity = enderecoRepository.save(endereco);
        return usuarioConverter.paraEnderecoDTO(enderecoEntity);
    }

    public TelefoneDTO cadastraTelefone(String token, TelefoneDTO dto){
        String email = jwtUtil.extractUsername(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException(ERROR_PREFIX));
        Telefone telefone = usuarioConverter.paraTelefoneEntity(dto, usuario.getId());
        Telefone telefoneEntity = telefoneRepository.save(telefone);
        return usuarioConverter.paraTelefoneDTO(telefoneEntity);
    }
}
