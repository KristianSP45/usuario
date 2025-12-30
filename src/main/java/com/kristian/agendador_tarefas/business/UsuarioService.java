package com.kristian.agendador_tarefas.business;

import com.kristian.agendador_tarefas.business.DTO.UsuarioDTO;
import com.kristian.agendador_tarefas.business.converter.UsuarioConverter;
import com.kristian.agendador_tarefas.infrastructure.entity.Usuario;
import com.kristian.agendador_tarefas.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;

    public UsuarioDTO salvarUsuario(UsuarioDTO usuarioDTO){
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);


        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));

        //OU  usuario = usuarioRepository.save(usuario);
        //return usuarioConverter.paraUsuarioDTO(usuario);
    }
}
