package com.kristian.usuario.business;

import com.kristian.usuario.infrastructure.clients.ViaCepClient;
import com.kristian.usuario.infrastructure.clients.ViaCepDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ViaCepService {

    private final ViaCepClient client;

    public ViaCepDTO buscarDadosEndereco(String cep){
        return client.buscaDadosEndereco(processarCep(cep));
    }

    public String processarCep(String cep){
        String cepFormatado = cep.replace(" ", "").
                replace("-", "");

        if (!cepFormatado.matches("\\d+") || !Objects.equals(cepFormatado.length(), 8)){
            throw new IllegalArgumentException("O cep contém caracteres inválidos, favor verificar");
        }
        return cepFormatado;
    }
}
