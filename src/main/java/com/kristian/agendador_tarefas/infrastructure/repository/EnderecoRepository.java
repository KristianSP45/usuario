package com.kristian.agendador_tarefas.infrastructure.repository;

import com.kristian.agendador_tarefas.infrastructure.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}
