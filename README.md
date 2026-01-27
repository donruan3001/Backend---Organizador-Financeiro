Aqui está um **resumo do README**, mantendo **apenas os pontos realmente essenciais** para quem bate o olho no projeto (ideal para GitHub):

---

## 💰 Organizador Financeiro – API REST

API RESTful para **gerenciamento financeiro pessoal**, desenvolvida com **Spring Boot**, focada em **segurança, organização e regras de negócio sólidas**.

### 🎯 Objetivo

Permitir que usuários controlem **contas bancárias, receitas e despesas**, com **autenticação JWT** e **autorização por roles**, garantindo que cada usuário acesse apenas seus próprios dados.

---

## 🚀 Tecnologias Principais

* **Java 17**
* **Spring Boot 3**
* **Spring Security + JWT**
* **Spring Data JPA / Hibernate**
* **MySQL 8**
* **Flyway**
* **Docker**
* **Swagger / OpenAPI**

---

## ⚙️ Funcionalidades Essenciais

### 🔐 Autenticação e Segurança

* Registro e login de usuários
* Autenticação **JWT stateless**
* Criptografia de senha com **BCrypt**
* Controle de acesso por **roles (USER / ADMIN)**

### 💳 Contas Bancárias

* CRUD de contas (corrente, poupança, investimento)
* Contas vinculadas ao usuário autenticado
* Validação de propriedade dos dados

### 💸 Transações Financeiras

* Registro de **receitas e despesas**
* Atualização automática do saldo da conta
* Validação de saldo suficiente para despesas
* Reversão correta de saldo em update/delete

### 👨‍💼 Administração

* Listagem e gerenciamento de contas de todos os usuários
* Acesso exclusivo para **ADMIN**

---

## 🏗️ Arquitetura

* Arquitetura em **camadas**:

  * Controllers
  * Services (regras de negócio)
  * Repositories
  * Database
* Uso de **DTOs**, **exceções globais** e **validações**

---

## 🔌 Principais Endpoints

* `/auth/register`, `/auth/login`, `/auth/me`
* `/accounts` (CRUD)
* `/api/transactions` (CRUD)
* `/admin/accounts` (ADMIN)

---

## 🐳 Execução

```bash
docker compose up --build
```

Aplicação disponível em:
`http://localhost:8080`

---

## 🗄️ Banco de Dados

* Relacionamento entre **users → accounts → transactions**
* Controle de auditoria (created_at / updated_at)

---

**Projeto focado em boas práticas de backend, segurança e regras de negócio reais.**
⭐ Se gostou, deixe uma estrela!

---

Se quiser, posso:

* Criar uma **versão ultra curta** (para README minimalista)
* Adaptar o texto para **portfólio ou LinkedIn**
* Deixar o README mais **profissional para recrutadores**
