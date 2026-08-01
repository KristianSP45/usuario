# usuario

API REST para gerenciamento de **usuários, endereços e telefones**, permitindo autenticação com **JWT**, cadastro de dados relacionados ao usuário e consulta automática de endereços através da **API ViaCEP**.

Este serviço faz parte do ecossistema **Agendador de Tarefas**, sendo responsável pelo gerenciamento e autenticação de usuários utilizados pelos demais microsserviços.

## Tecnologias

* Java 21
* Spring Boot 3.5.9
* Spring Web
* Spring Data JPA
* Spring Security
* JWT (JJWT)
* OpenFeign
* PostgreSQL
* Swagger/OpenAPI
* Lombok
* SonarQube
* Gradle

## Melhorias implementadas

* Autenticação e autorização com **JWT**
* Integração com **API ViaCEP** utilizando **OpenFeign**
* Documentação automática com **Swagger/OpenAPI**
* Tratamento global de exceções
* Conversão entre DTOs e entidades
* Atualização parcial de usuários, endereços e telefones
* Configuração de **SonarQube** para análise estática
* Workflows de automação com **GitHub Actions**

## Endpoints

### Usuários

| Método | Endpoint              | Descrição                              |
| ------ | --------------------- | -------------------------------------- |
| POST   | `/usuario`               | Cadastrar usuário                      |
| POST   | `/usuario/login`         | Autenticar usuário e gerar token JWT   |
| GET    | `/usuario?email={email}` | Buscar usuário por e-mail              |
| PUT    | `/usuario`               | Atualizar dados do usuário autenticado |
| DELETE | `/usuario/{email}`       | Remover usuário pelo e-mail            |

### Endereços

| Método | Endpoint                 | Descrição                                     |
| ------ | ------------------------ | --------------------------------------------- |
| POST   | `/usuario/endereco`         | Cadastrar endereço para o usuário autenticado |
| PUT    | `/usuario/endereco?id={id}` | Atualizar endereço                            |
| GET    | `/usuario/endereco/{cep}`   | Consultar endereço na API ViaCEP              |

### Telefones

| Método | Endpoint                 | Descrição                                     |
| ------ | ------------------------ | --------------------------------------------- |
| POST   | `/usuario/telefone`         | Cadastrar telefone para o usuário autenticado |
| PUT    | `/usuario/telefone?id={id}` | Atualizar telefone                            |

### Exemplo de login

```json
{
  "email": "usuario@email.com",
  "senha": "123456"
}
```

## Segurança

A API utiliza **Spring Security + JWT** com autenticação baseada em token Bearer.

### Header obrigatório para rotas protegidas

```http
Authorization: Bearer <token>
```

O usuário autenticado é identificado automaticamente pelo e-mail contido no token JWT.

## Integração externa

A aplicação possui integração com a **API ViaCEP** para consulta automática de endereços a partir do CEP informado.

### Exemplo

```http
GET /usuario/endereco/72110200
```

## Regras de negócio

* O **e-mail do usuário deve ser único**
* Senhas são armazenadas de forma **criptografada** com `PasswordEncoder`
* Apenas usuários autenticados podem cadastrar ou atualizar endereços e telefones
* O CEP informado é validado antes da consulta ao ViaCEP
* Atualizações preservam os dados existentes quando um campo não é enviado

## Tratamento de exceções

* `ResourceNotFoundException` → 404
* `ConflictException` → 409
* `IllegalArgumentException` → 400
* `UnauthorizedException` → 401
* `InternalServerErrorException` → 500
* `GlobalExceptionHandler` → padronização das respostas de erro

## Testes

No momento, esta API **não possui testes unitários implementados**.

---

## Automação / CI

O projeto possui **workflows do GitHub Actions** configurados para automação de tarefas de integração contínua, como:

* execução automática do build;
* validação do projeto em pushes e pull requests;
* integração com ferramentas de qualidade, como **SonarQube**.

## Como executar

```bash
git clone https://github.com/KristianSP45/usuario
cd usuario
./gradlew bootRun
```

A aplicação ficará disponível em:

```
http://localhost:8080
```

## Swagger

Após iniciar a aplicação, acesse:

```
http://localhost:8080/swagger-ui/index.html
```

## Docker

O projeto possui suporte para **Docker** e **Docker Compose**.

### Comandos principais

```bash
docker-compose up -d
docker-compose down
```

## Observações

* Projeto desenvolvido durante um **curso prático de Spring Boot e Microsserviços**, acompanhando as aulas e realizando implementações junto à instrutora.
* Utilizado para aprendizado de **Spring Security, JWT, JPA, OpenFeign, Docker, SonarQube e comunicação entre microsserviços**.
* Este serviço representa a **API central de autenticação e gerenciamento de usuários** do ecossistema do projeto.

## Autor

**Kristian Pessoa**
