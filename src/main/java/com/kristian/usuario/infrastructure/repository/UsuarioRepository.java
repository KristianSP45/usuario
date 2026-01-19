package com.kristian.usuario.infrastructure.repository;

import com.kristian.usuario.infrastructure.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository//Marca como componente do Spring, Específico para banco de dados
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {//extends JpaRepository<Usuario, Long> =
    // “Spring, crie para mim uma classe que saiba mexer no banco usando a Entity Usuario, cujo ID é Long.”
    // obs: só existe metodos prontos como save() e delete porque tem esse extends

    boolean existsByEmail(String email);

    Optional<Usuario> findByEmail(String email);//Optional<> = usado para evitar NullPointerException(erro)
    //e fazer o tratamento de erro na service

    @Transactional //controla uma transação (tudo ou nada).
    // Se alguma operação no banco falhar, todas as anteriores são desfeitas (rollback).
    // Pode ser usado em Service ou Repository, mas a boa prática é usar no Service
    // quando ele coordena mais de uma operação no banco (save, delete, update).
    void deleteByEmail(String email);
}
