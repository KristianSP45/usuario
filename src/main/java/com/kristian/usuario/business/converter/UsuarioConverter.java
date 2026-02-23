package com.kristian.usuario.business.converter;

import com.kristian.usuario.business.dto.EnderecoDTO;
import com.kristian.usuario.business.dto.TelefoneDTO;
import com.kristian.usuario.business.dto.UsuarioDTO;
import com.kristian.usuario.infrastructure.entity.Endereco;
import com.kristian.usuario.infrastructure.entity.Telefone;
import com.kristian.usuario.infrastructure.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component//@Component = “Spring, essa classe existe e você pode injetar”
//@Component é de uso generico, ex: @Service é proprio para service, e @Component seria para classes auxiliares
//Se a classe precisa ser injetada e não é Controller, Service ou Repository → use @Component.
public class UsuarioConverter {

    //DTO para Entity

    public Usuario paraUsuario(UsuarioDTO usuarioDTO){// Usado quando:
        // chegou JSON, virou DTO, precisa virar Entity para salvar no banco
        return Usuario.builder()//Builder (por quê?):
                // Evita construtor gigante, deixa claro o que está sendo setado, muito usado com JPA + Lombok
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .senha(usuarioDTO.getSenha())
                .enderecos(usuarioDTO.getEnderecos() != null ?
                        paraListaEndereco(usuarioDTO.getEnderecos()) : null)
                .telefones(usuarioDTO.getTelefones() != null ?
                        paraListaTelefone(usuarioDTO.getTelefones()) : null)

                .build();
    }

    //paraListaEndereco() → converte muitos
    public List<Endereco> paraListaEndereco(List<EnderecoDTO> enderecoDTOS){//enderecoDTOS > lista original
        List<Endereco> enderecos = new ArrayList<>();//Nova lista

        for (EnderecoDTO enderecoDTO : enderecoDTOS){//EnderecoDTO > tipo / enderecoDTO > variável temporária
            enderecos.add(paraEndereco(enderecoDTO));
        }
        return enderecos;

        //OU (Forma mais avançada)  return enderecoDTOS.stream().map(this::paraEndereco).toList();
    }

    //paraEndereco() → converte um
    public Endereco paraEndereco(EnderecoDTO enderecoDTO){
        return Endereco.builder()
                .rua(enderecoDTO.getRua())
                .numero(enderecoDTO.getNumero())
                .cidade(enderecoDTO.getCidade())
                .complemento(enderecoDTO.getComplemento())
                .cep(enderecoDTO.getCep())
                .estado(enderecoDTO.getEstado())

                .build();
    }

    public List<Telefone> paraListaTelefone(List<TelefoneDTO> telefoneDTOS){
        return  telefoneDTOS.stream().map(this::paraTelefone).toList();//Telefones com Stream:
        // Mesma coisa do for, só mais elegante. Conceito é o mesmo: DTO > Entity
    }

    public Telefone paraTelefone(TelefoneDTO telefoneDTO){
        return Telefone.builder()
                .id(telefoneDTO.getId())
                .numero(telefoneDTO.getNumero())
                .ddd(telefoneDTO.getDdd())

                .build();
    }

    //FIM
    //---------------------------------------------------------------
    //Entity para DTO

    public UsuarioDTO paraUsuarioDTO(Usuario usuarioDTO){//Usado quando:
        // Banco devolveu Entity, precisa responder a API
        return UsuarioDTO.builder()
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .senha(usuarioDTO.getSenha())
                .enderecos(usuarioDTO.getEnderecos() != null ?
                        paraListaEnderecoDTO(usuarioDTO.getEnderecos()) : null)
                .telefones(usuarioDTO.getTelefones() != null ?
                        paraListaTelefoneDTO(usuarioDTO.getTelefones()) : null)

                .build();
    }

    public List<EnderecoDTO> paraListaEnderecoDTO(List<Endereco> enderecoDTOS){
        List<EnderecoDTO> enderecos = new ArrayList<>();

        for (Endereco enderecoDTO : enderecoDTOS){
            enderecos.add(paraEnderecoDTO(enderecoDTO));
        }
        return enderecos;
    }

    public EnderecoDTO paraEnderecoDTO(Endereco endereco){
        return EnderecoDTO.builder()
                .id(endereco.getId())//Agora o ID aparece. Porque agora estamos retornando, não salvando
                // e porque precisa saber qual o endereço e telefone vai ser usado
                .rua(endereco.getRua())
                .numero(endereco.getNumero())
                .cidade(endereco.getCidade())
                .complemento(endereco.getComplemento())
                .cep(endereco.getCep())
                .estado(endereco.getEstado())

                .build();
    }

    public List<TelefoneDTO> paraListaTelefoneDTO(List<Telefone> telefoneDTOS){
        return  telefoneDTOS.stream().map(this::paraTelefoneDTO).toList();
    }

    public TelefoneDTO paraTelefoneDTO(Telefone telefone){
        return TelefoneDTO.builder()
                .id(telefone.getId())
                .numero(telefone.getNumero())
                .ddd(telefone.getDdd())

                .build();
    }

    //FIM
    //------------------------------------------------------

    public Usuario updateUsuario(UsuarioDTO usuarioDTO, Usuario entity){
        return Usuario.builder()
                .nome(usuarioDTO.getNome() !=null ? usuarioDTO.getNome() : entity.getNome())
        //“Se o DTO trouxe esse campo, usa ele. Se não trouxe, mantém o do banco.”
                .id(entity.getId())
                .senha(usuarioDTO.getSenha() !=null ? usuarioDTO.getSenha() : entity.getSenha())
                .email(usuarioDTO.getEmail() !=null ? usuarioDTO.getEmail() : entity.getEmail())
                .enderecos(entity.getEnderecos())
                .telefones(entity.getTelefones())

                .build();
    }

    public Endereco updateEndereco(EnderecoDTO dto, Endereco entity){
        return  Endereco.builder()
                .id(entity.getId())
                .rua(dto.getRua() != null ? dto.getRua() : entity.getRua())
                .numero(dto.getNumero() != null ? dto.getNumero() : entity.getNumero())
                .complemento(dto.getComplemento() != null ? dto.getComplemento() : entity.getComplemento())
                .cidade(dto.getCidade() != null ? dto.getCidade() : entity.getCidade())
                .estado(dto.getEstado() != null ? dto.getEstado() : entity.getEstado())
                .cep(dto.getCep() != null ? dto.getCep() : entity.getCep())
                .usuario_id(entity.getUsuario_id())
                .build();
    }

    public Telefone updateTelefone(TelefoneDTO dto, Telefone entity){
        return Telefone.builder()
                .id(entity.getId())
                .ddd(dto.getDdd() != null ? dto.getDdd() : entity.getDdd())
                .numero(dto.getNumero() != null ? dto.getNumero() : entity.getNumero())
                .usuario_id(entity.getUsuario_id())
                .build();
    }

    public Endereco paraEnderecoEntity(EnderecoDTO dto, Long idUsuario){
        return Endereco.builder()
                .rua(dto.getRua())
                .numero(dto.getNumero())
                .complemento(dto.getComplemento())
                .cidade(dto.getCidade())
                .estado(dto.getEstado())
                .cep(dto.getCep())
                .usuario_id(idUsuario)
                //O DTO não sabe quem é o usuário. O Service sabe (via token)

                .build();
    }

    public Telefone paraTelefoneEntity(TelefoneDTO dto, Long idUsuario){
        return Telefone.builder()
                .numero(dto.getNumero())
                .ddd(dto.getDdd())
                .usuario_id(idUsuario)

                .build();
    }
}