# sistema-registros-academicos

Este projeto utiliza **Quarkus** — o Supersonic Subatomic Java Framework.

Para saber mais sobre Quarkus, visite: [https://quarkus.io/](https://quarkus.io/).

---

## Índice

* [Sobre](#sobre)
* [Pré-requisitos](#pr%C3%A9-requisitos)
* [🚀 Passo a Passo para Execução (Setup Local)](#-passo-a-passo-para-execu%C3%A7%C3%A3o-setup-local)

  * [1. Banco de Dados (MySQL)](#1-banco-de-dados-mysql)
  * [2. Variáveis de Ambiente (.env)](#2-vari%C3%A1veis-de-ambiente-env)
  * [3. Geração das Chaves JWT (RSA)](#3-gera%C3%A7%C3%A3o-das-chaves-jwt-rsa)
  * [4. Executando a aplicação em modo `dev`](#4-executando-a-aplica%C3%A7%C3%A3o-em-modo-dev)
* [Packaging e execução](#packaging-e-execu%C3%A7%C3%A3o)

  * [Über-jar (opcional)](#%C3%BCber-jar-opcional)
  * [Executável nativo (native)](#execut%C3%A1vel-nativo-native)
* [Guides relacionados (Quarkus)](#guides-relacionados-quarkus)
* [Código fornecido / pontos de partida](#c%C3%B3digo-fornecido--pontos-de-partida)

## Sobre

Projeto base de um sistema de registros acadêmicos construído com Quarkus. Inclui autenticação JWT (com RSA), persistência via Hibernate ORM e configuração externa via arquivo `.env` seguindo a metodologia Twelve-Factor App.

## Pré-requisitos

* Java JDK (versão 21)
* Maven (ou usar o `mvnw` incluído)
* MySQL instalado e em execução
* OpenSSL (para gerar chaves RSA)

## 🚀 Passo a Passo para Execução (Setup Local)

### 1. Banco de Dados (MySQL)

Certifique-se de ter o MySQL instalado e rodando.

Crie um schema em branco chamado `ccens_db`. O Hibernate cuidará da criação das tabelas:

```sql
CREATE DATABASE ccens_db;
```

### 2. Variáveis de Ambiente (.env)

A aplicação utiliza a metodologia do Twelve-Factor App para ocultar credenciais.

Crie um arquivo chamado `.env` na raiz do projeto (na mesma pasta do `pom.xml`) e adicione as seguintes variáveis:

```env
# Configurações do Banco de Dados
DB_URL=jdbc:mysql://localhost:3306/ccens_db
DB_USER=seu_usuario_mysql
DB_PASS=sua_senha_mysql

# Caminho absoluto ou relativo da sua Chave Privada JWT (PKCS8)
JWT_PRIVATE_KEY_PATH=privateKey.pem
```

> Obs.: Substitua `seu_usuario_mysql` e `sua_senha_mysql` pelos valores corretos do seu ambiente.

### 3. Geração das Chaves JWT (RSA)

Gere o par de chaves JWT executando os comandos do OpenSSL no seu terminal. Atenção (usuários de Windows): Utilize um emulador de terminal com ambiente Linux, como o Git Bash ou o WSL, para que os comandos do OpenSSL funcionem corretamente.

Gere a chave privada e converta para PKCS8 (salve na raiz do projeto como `privateKey.pem`):

```bash
openssl genrsa -out rsaPrivateKey.pem 2048
openssl pkcs8 -topk8 -nocrypt -inform pem -in rsaPrivateKey.pem -outform pem -out privateKey.pem
```

Gere a chave pública (salve em `src/main/resources` como `publicKey.pem`):

```bash
openssl rsa -in rsaPrivateKey.pem -pubout -out publicKey.pem
```

Exclua o arquivo necessário para gerar a chave pública:

```bash
rm rsaPrivateKey.pem
```

### 4. Executando a aplicação em modo `dev`

Você pode rodar a aplicação em modo de desenvolvimento (habilita live coding) com:

```bash
./mvnw quarkus:dev
```

> NOTE: O Quarkus fornece uma Dev UI disponível apenas em modo dev: `http://localhost:8080/q/dev/`. Também é possível acessar a documentação da API: `http://localhost:8080/q/swagger-ui/`.

Ao iniciar a aplicação com sucesso em modo `dev`, o schema será criado. Para acessar a aplicação, será necessário a inserção manual no banco de dados, através do arquivo `insertUser.sql`.

## Packaging e execução

Para empacotar a aplicação:

```bash
./mvnw package
```

Isso gera o arquivo `quarkus-run.jar` em `target/quarkus-app/`. Observe que não é um über-jar: as dependências ficam em `target/quarkus-app/lib/`.

Execute a aplicação com:

```bash
java -jar target/quarkus-app/quarkus-run.jar
```

### Über-jar (opcional)

Se preferir um único JAR (über-jar):

```bash
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

Depois, execute com:

```bash
java -jar target/*-runner.jar
```

### Executável nativo (native)

Crie um executável nativo com:

```bash
./mvnw package -Dnative
```

Se você não possui GraalVM localmente, pode usar o build nativo em container:

```bash
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

Após a geração, execute o binário nativo (exemplo):

```bash
./target/sistema-registros-academicos-1.0.0-SNAPSHOT-runner
```

Para mais detalhes sobre builds nativos, consulte a documentação do Quarkus: [https://quarkus.io/guides/maven-tooling](https://quarkus.io/guides/maven-tooling)

## Guides relacionados (Quarkus)

* REST (Jakarta REST + Vert.x)
* SmallRye OpenAPI (documentação das APIs com Swagger UI)
* REST Jackson (serialização Jackson para Quarkus REST)
* Hibernate ORM with Panache (simplifica persistência com JPA)
* SmallRye JWT (segurança com JSON Web Token)
* JDBC Driver - MySQL (conexão MySQL via JDBC)

## Código fornecido / pontos de partida

* **Hibernate ORM** — exemplos de criação de entidades JPA e uso com Panache.
* **REST** — endpoints iniciais para criar/ler/atualizar/excluir recursos.

(Ver seções e pacotes do projeto para exemplos e guias específicos.)

