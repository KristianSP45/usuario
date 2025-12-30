package com.kristian.agendador_tarefas.business.DTO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioDTO {

    private String nome;
    private  String email;
    private String senha;
    private List<EnderecoDTO> endereco;
    private List<TelefoneDTO> telefone;
}
