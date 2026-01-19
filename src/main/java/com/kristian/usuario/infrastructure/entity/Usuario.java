package com.kristian.usuario.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor//@AllArgsConstructor > construtor com tudo
@NoArgsConstructor//@NoArgsConstructor > construtor vazio (JPA PRECISA)
@Entity//“Essa classe vira uma tabela no banco”
@Table(name = "usuario")//define o nome real da tabela(nome que foi criado), evita o JPA criar nome automático
@Builder//@Builder > criação limpa de objetos. @Builder é responsável pelo .builder().build()
public class Usuario implements UserDetails {// implements UserDetails permite que o Spring Security
// utilize essa entidade como um usuário autenticável,
// exigindo a implementação de getUsername, getPassword e getAuthorities

    @Id//= chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY)//IDENTITY > o banco gera o ID, normalmente AUTO_INCREMENT
    private Long id;
    @Column(name = "nome", length = 100)//Diz ao spring que é uma coluna e que o nome é o mesmo do banco
    private String nome;
    @Column(name = "email", length = 100)
    private String email;
    @Column(name = "senha")
    private String senha;
    @OneToMany(cascade = CascadeType.ALL)//@OneToMany = 1 p N / um usuario para varios enderecos
    //cascade = CascadeType.ALL = “faz a mesma coisa que for fazer com usuário”,
    //Salvar, atualizar ou deletar: usuário > filhos acompanham
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    //= “existe uma coluna no banco que aponta para o id do usuário”
    private List<Endereco> enderecos;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private List<Telefone> telefones;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {//getAuthorities() = Permissões / roles
        return List.of();//Esse usuário não tem permissões
    }

    @Override
    public String getPassword() {//getPassword() = Senha criptografada
        return senha;
    }

    @Override
    public String getUsername() {
        return email;//Identificador do login (email como padrão)
    }
}
