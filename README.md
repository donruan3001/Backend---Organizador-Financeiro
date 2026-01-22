# 💰 Organizador Financeiro - API REST

Sistema completo de gerenciamento financeiro pessoal desenvolvido com Spring Boot, permitindo controle de contas bancárias, transações e análise de movimentações financeiras.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.1-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

---

## 📋 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Funcionalidades](#-funcionalidades)
- [Arquitetura](#-arquitetura)
- [Como Funciona](#-como-funciona)
- [Instalação e Configuração](#-instalação-e-configuração)
- [Endpoints da API](#-endpoints-da-api)
- [Segurança](#-segurança)
- [Banco de Dados](#-banco-de-dados)
- [Testes](#-testes)
- [Roadmap](#-roadmap)
- [Contribuindo](#-contribuindo)
- [Licença](#-licença)

---

## 🎯 Sobre o Projeto

O **Organizador Financeiro** é uma API RESTful robusta que permite aos usuários gerenciar suas finanças pessoais de forma eficiente e segura. O sistema oferece controle completo sobre contas bancárias, transações de receitas e despesas, com autenticação JWT e autorização baseada em roles.

### Características Principais

- ✅ **Autenticação JWT** - Sistema seguro de autenticação com tokens
- ✅ **Controle de Contas** - Gerenciamento de múltiplas contas bancárias
- ✅ **Transações** - Registro de receitas e despesas com categorização
- ✅ **Autorização** - Usuários só acessam seus próprios dados
- ✅ **Validação** - Validação robusta de dados de entrada
- ✅ **Auditoria** - Rastreamento de criação e modificação de registros
- ✅ **API Documentada** - Swagger/OpenAPI para documentação interativa

---

## 🚀 Tecnologias Utilizadas

### Backend Framework
- **Java 17** - Linguagem de programação
- **Spring Boot 3.4.1** - Framework principal
- **Spring Web** - Construção de APIs REST
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Autenticação e autorização
- **Spring Validation** - Validação de dados

### Banco de Dados
- **MySQL 8.0** - Banco de dados relacional
- **Flyway** - Controle de migrations
- **Hibernate** - ORM (Object-Relational Mapping)

### Segurança
- **JWT (JSON Web Token)** - Autenticação stateless
- **BCrypt** - Criptografia de senhas
- **Spring Security** - Framework de segurança

### Documentação
- **Swagger/OpenAPI** - Documentação interativa da API
- **SpringDoc** - Geração automática de documentação

### Ferramentas de Desenvolvimento
- **Maven** - Gerenciamento de dependências
- **Docker** - Containerização (configurado)

---

## ⚙️ Funcionalidades

### 🔐 Autenticação e Autorização

#### Registro de Usuário
```http
POST /auth/register
```
- Cria nova conta de usuário
- Senha criptografada com BCrypt
- Validação de email único

#### Login
```http
POST /auth/login
```
- Autentica usuário com email/senha
- Retorna token JWT válido por tempo configurável
- Token usado em todas as requisições subsequentes

#### Perfil do Usuário
```http
GET /auth/me
```
- Retorna informações do usuário autenticado
- Requer token JWT válido

---

### 💳 Gerenciamento de Contas

#### Criar Conta
```http
POST /accounts
```
- Cria conta bancária vinculada ao usuário autenticado
- Tipos: CORRENTE, POUPANCA, INVESTIMENTO
- Saldo inicial configurável

#### Listar Contas
```http
GET /accounts
```
- Lista todas as contas do usuário autenticado
- Retorna: ID, nome, tipo, saldo, data de criação

#### Buscar Conta por ID
```http
GET /accounts/{id}
```
- Retorna detalhes de uma conta específica
- Valida propriedade (somente dono acessa)

#### Atualizar Conta
```http
PUT /accounts/{id}
```
- Atualiza nome, tipo ou saldo da conta
- Validação de saldo positivo
- Somente dono pode atualizar

#### Deletar Conta
```http
DELETE /accounts/{id}
```
- Remove conta do usuário
- Cascata: deleta transações associadas
- Somente dono pode deletar

---

### 💸 Gerenciamento de Transações

#### Criar Transação
```http
POST /api/transactions
```
- Registra nova transação (receita ou despesa)
- Tipos: INCOME (receita), EXPENSE (despesa)
- Atualiza saldo da conta automaticamente
- Valida saldo suficiente para despesas

#### Listar Transações
```http
GET /api/transactions
```
- Lista todas as transações do usuário
- Ordenadas por data (mais recentes primeiro)
- Inclui transações de todas as contas do usuário

#### Buscar Transação por ID
```http
GET /api/transactions/{id}
```
- Retorna detalhes de transação específica
- Valida propriedade através da conta

#### Listar Transações por Conta
```http
GET /api/transactions/account/{accountId}
```
- Filtra transações de uma conta específica
- Útil para extratos por conta

#### Atualizar Transação
```http
PUT /api/transactions/{id}
```
- Atualiza nome, categoria, tipo ou valor
- Recalcula saldo da conta (reverte antiga, aplica nova)
- Valida saldo suficiente após atualização

#### Deletar Transação
```http
DELETE /api/transactions/{id}
```
- Remove transação
- Reverte efeito no saldo da conta
- Somente dono pode deletar

---

### 👨‍💼 Funcionalidades Admin

#### Listar Todas as Contas (Admin)
```http
GET /admin/accounts
```
- Lista contas de todos os usuários (paginado)
- Requer role ADMIN
- Inclui informações do proprietário

#### Atualizar Conta (Admin)
```http
PATCH /admin/{id}
```
- Permite admin modificar qualquer conta
- Validação de saldo positivo

#### Deletar Conta (Admin)
```http
DELETE /admin/{id}
```
- Admin pode deletar qualquer conta
- Cascata: deleta transações associadas

---

## 🏗️ Arquitetura

### Arquitetura em Camadas

```
┌─────────────────────────────────────────┐
│          Controllers Layer              │
│  (REST Endpoints, Request/Response)     │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│           Services Layer                │
│  (Business Logic, Validations)          │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│         Repositories Layer              │
│  (Data Access, JPA Queries)             │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│            Database Layer               │
│  (MySQL, Persistence)                   │
└─────────────────────────────────────────┘
```

### Componentes Principais

#### 1. **Controllers** (`finance.controllers`)
- `ControllerAuth` - Autenticação e registro
- `ControllerAccount` - Operações de contas (usuário)
- `ControllerTransactions` - Operações de transações
- `ControllerAdmin` - Operações administrativas

#### 2. **Services** (`finance.services`)
- `ServiceAuth` - Lógica de autenticação e JWT
- `ServiceAccount` - Regras de negócio de contas
- `ServiceTransactions` - Lógica de transações e saldo
- `ServiceAdmin` - Operações administrativas

#### 3. **Repositories** (`finance.repository`)
- `RepositoryUser` - Acesso a dados de usuários
- `RepositoryAccount` - Acesso a dados de contas
- `RepositoryTransactions` - Acesso a dados de transações

#### 4. **Entities** (`finance.domain`)
- `User` - Entidade de usuário
- `Account` - Entidade de conta bancária
- `Transaction` - Entidade de transação financeira

#### 5. **DTOs** (`finance.domain.dto`)
- **User**: `UserRegisterDTO`, `UserLoginDTO`, `UserProfileDTO`, `ResponseJwtDTO`
- **Account**: `AccountCreateDTO`, `AccountUpdateDTO`, `AccountResponseDTO`
- **Transaction**: `TransactionCreateDTO`, `TransactionUpdateDTO`, `TransactionResponseDTO`

#### 6. **Security** (`finance.config`)
- `SecurityConfiguration` - Configuração do Spring Security
- `JWTService` - Geração e validação de tokens JWT
- `JWTFilter` - Filtro de autenticação em requisições
- `AuthenticatedUser` - Utilitário para obter usuário autenticado

#### 7. **Exceptions** (`finance.exceptions`)
- `GlobalControllerExceptionHandler` - Tratamento global de exceções
- `AccountNotFoundException` - Conta não encontrada
- `UserNotFoundException` - Usuário não encontrado
- `UnauthorizedAccessException` - Acesso não autorizado
- `InsufficientBalanceException` - Saldo insuficiente
- `BusinessException` - Regra de negócio violada
- `DuplicateResourceException` - Recurso duplicado

---

## 🔄 Como Funciona

### Fluxo de Autenticação

```
1. Usuário → POST /auth/register
   ↓
2. Sistema criptografa senha (BCrypt)
   ↓
3. Salva usuário no banco de dados
   ↓
4. Usuário → POST /auth/login (email + senha)
   ↓
5. Sistema valida credenciais
   ↓
6. Sistema gera token JWT com ID do usuário
   ↓
7. Retorna token ao cliente
   ↓
8. Cliente inclui token em todas as requisições:
   Header: Authorization: Bearer {token}
```

### Fluxo de Criação de Transação

```
1. Cliente → POST /api/transactions
   {
     "accountId": 1,
     "name": "Salário",
     "category": "SALARY",
     "type": "INCOME",
     "amount": 5000.00
   }
   ↓
2. JWTFilter extrai userId do token
   ↓
3. ServiceTransactions busca conta por ID
   ↓
4. Valida se usuário é dono da conta
   ↓
5. Se EXPENSE: Valida saldo suficiente
   ↓
6. Atualiza saldo da conta:
   - INCOME: saldo += amount
   - EXPENSE: saldo -= amount
   ↓
7. Salva transação no banco
   ↓
8. Retorna TransactionResponseDTO
```

### Fluxo de Atualização de Transação

```
1. Cliente → PUT /api/transactions/{id}
   ↓
2. Sistema busca transação existente
   ↓
3. Valida propriedade (dono da conta)
   ↓
4. REVERTE efeito da transação antiga no saldo
   ↓
5. Aplica valores atualizados na transação
   ↓
6. APLICA efeito da transação nova no saldo
   ↓
7. Valida saldo resultante (não negativo)
   ↓
8. Salva alterações no banco
   ↓
9. Retorna transação atualizada
```

---

## 📦 Instalação e Configuração

### Pré-requisitos

- **Java 17** ou superior
- **Maven 3.8+**
- **MySQL 8.0+**
- **Docker** (opcional, para containerização)

### 1. Clonar o Repositório

```bash
git clone https://github.com/seu-usuario/Backend---Organizador-Financeiro.git
cd Backend---Organizador-Financeiro
```

### 2. Configurar Banco de Dados

Crie um banco de dados no MySQL:

```sql
CREATE DATABASE finance_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Configurar Variáveis de Ambiente

Edite `src/main/resources/application.properties`:

```properties
# Banco de Dados
spring.datasource.url=jdbc:mysql://localhost:3306/finance_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

# JWT Secret (IMPORTANTE: Altere em produção!)
api.security.token.secret=my-secret-key-change-in-production

# Configurações de desenvolvimento
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
```

### 4. Executar Migrations (Flyway)

As migrations são executadas automaticamente na inicialização:

```bash
mvn flyway:migrate
```

### 5. Compilar e Executar

```bash
# Compilar
mvn clean install

# Executar
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

### 6. Acessar Documentação Swagger

Abra no navegador:
```
http://localhost:8080/swagger-ui.html
```

---

## 🔌 Endpoints da API

### Autenticação

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| POST | `/auth/register` | Registra novo usuário | ❌ |
| POST | `/auth/login` | Autentica usuário | ❌ |
| GET | `/auth/me` | Retorna perfil do usuário | ✅ |

### Contas

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| POST | `/accounts` | Cria nova conta | ✅ |
| GET | `/accounts` | Lista contas do usuário | ✅ |
| GET | `/accounts/{id}` | Busca conta por ID | ✅ |
| PUT | `/accounts/{id}` | Atualiza conta | ✅ |
| DELETE | `/accounts/{id}` | Deleta conta | ✅ |

### Transações

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| POST | `/api/transactions` | Cria transação | ✅ |
| GET | `/api/transactions` | Lista transações do usuário | ✅ |
| GET | `/api/transactions/{id}` | Busca transação por ID | ✅ |
| GET | `/api/transactions/account/{accountId}` | Filtra por conta | ✅ |
| PUT | `/api/transactions/{id}` | Atualiza transação | ✅ |
| DELETE | `/api/transactions/{id}` | Deleta transação | ✅ |

### Admin

| Método | Endpoint | Descrição | Auth | Role |
|--------|----------|-----------|------|------|
| GET | `/admin/accounts` | Lista todas as contas | ✅ | ADMIN |
| PATCH | `/admin/{id}` | Atualiza conta (admin) | ✅ | ADMIN |
| DELETE | `/admin/{id}` | Deleta conta (admin) | ✅ | ADMIN |

---

## 📝 Exemplos de Uso

### Registrar Usuário

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@example.com",
    "password": "senha123"
  }'
```

### Login

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@example.com",
    "password": "senha123"
  }'
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### Criar Conta

```bash
curl -X POST http://localhost:8080/accounts \
  -H "Authorization: Bearer SEU_TOKEN_JWT" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Conta Corrente",
    "type": "CORRENTE",
    "balance": 1000.00
  }'
```

### Criar Transação (Receita)

```bash
curl -X POST http://localhost:8080/api/transactions \
  -H "Authorization: Bearer SEU_TOKEN_JWT" \
  -H "Content-Type: application/json" \
  -d '{
    "accountId": 1,
    "name": "Salário Março",
    "category": "SALARY",
    "type": "INCOME",
    "amount": 5000.00
  }'
```

### Criar Transação (Despesa)

```bash
curl -X POST http://localhost:8080/api/transactions \
  -H "Authorization: Bearer SEU_TOKEN_JWT" \
  -H "Content-Type: application/json" \
  -d '{
    "accountId": 1,
    "name": "Supermercado",
    "category": "FOOD",
    "type": "EXPENSE",
    "amount": 350.00
  }'
```

---

## 🔒 Segurança

### Autenticação JWT

O sistema utiliza **JSON Web Tokens (JWT)** para autenticação stateless:

1. **Geração do Token**: Após login bem-sucedido, um token JWT é gerado contendo:
   - Subject: ID do usuário
   - Issuer: "finance-api"
   - Expiration: Configurável (padrão: 24 horas)

2. **Validação**: Em cada requisição, o `JWTFilter` valida o token:
   - Verifica assinatura
   - Verifica expiração
   - Extrai ID do usuário
   - Carrega dados do usuário
   - Configura contexto de segurança

### Autorização

- **USER** (padrão): Acesso a próprios recursos (contas e transações)
- **ADMIN**: Acesso total ao sistema, incluindo recursos de outros usuários

### Criptografia de Senhas

Senhas são criptografadas usando **BCrypt** com salt automático antes de serem armazenadas no banco de dados.

### CORS

CORS configurado para permitir requisições de:
- `http://localhost:5173` (Vite/React)
- `http://localhost:3000` (Create React App)
- `http://localhost:4200` (Angular)

Para produção, configure apenas origens confiáveis no `CorsConfig.java`.

---

## 🗄️ Banco de Dados

### Diagrama ER

```
┌─────────────────┐         ┌─────────────────┐         ┌─────────────────┐
│     users       │         │    accounts     │         │  transactions   │
├─────────────────┤         ├─────────────────┤         ├─────────────────┤
│ id (PK)         │────┐    │ id (PK)         │────┐    │ id (PK)         │
│ name            │    │    │ user_id (FK)    │    │    │ account_id (FK) │
│ username        │    └───→│ name            │    └───→│ category        │
│ password        │         │ type            │         │ name            │
│ role            │         │ balance         │         │ type            │
│ created_at      │         │ created         │         │ amount          │
│ updated_at      │         └─────────────────┘         │ created         │
└─────────────────┘                                     │ updated         │
                                                        └─────────────────┘
```

### Tabelas

#### `users`
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(10) NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### `accounts`
```sql
CREATE TABLE accounts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(120) NOT NULL,
    type VARCHAR(30) NOT NULL DEFAULT 'corrente',
    balance DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

#### `transactions`
```sql
CREATE TABLE transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_id BIGINT NOT NULL,
    category VARCHAR(50),
    name VARCHAR(80),
    type VARCHAR(20) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE
);
```

### Migrations

Migrations são gerenciadas pelo **Flyway**:

- `V1__CREATE.sql` - Criação inicial das tabelas
- `V2__ALTER.sql` - Alterações estruturais

---

## 🧪 Testes

### Executar Testes

```bash
mvn test
```

### Cobertura de Testes

```bash
mvn clean verify
```

Relatório gerado em: `target/site/jacoco/index.html`

### Tipos de Testes Recomendados

1. **Testes Unitários** - Services e utilidades
2. **Testes de Integração** - Controllers e Repositories
3. **Testes de Segurança** - Autenticação e autorização
4. **Testes de API** - Endpoints REST (Postman/REST Assured)

---

## 🛣️ Roadmap

### Versão 1.1 (Próxima Release)
- [ ] Paginação em todos os endpoints de listagem
- [ ] Filtros avançados (data, categoria, valor)
- [ ] Ordenação customizável
- [ ] Exportação de relatórios (PDF, Excel)

### Versão 1.2
- [ ] Notificações por email
- [ ] Gráficos e dashboards de análise
- [ ] Metas financeiras
- [ ] Orçamentos mensais

### Versão 2.0
- [ ] Suporte a múltiplas moedas
- [ ] Integração com bancos (Open Banking)
- [ ] App mobile (Flutter/React Native)
- [ ] Planejamento financeiro com IA

---

## 📊 Categorias de Transação

### Categorias Disponíveis

- **SALARY** - Salário
- **FREELANCE** - Trabalho freelance
- **INVESTMENT** - Investimentos
- **FOOD** - Alimentação
- **TRANSPORT** - Transporte
- **HOUSING** - Moradia
- **HEALTH** - Saúde
- **EDUCATION** - Educação
- **ENTERTAINMENT** - Entretenimento
- **SHOPPING** - Compras
- **BILLS** - Contas e serviços
- **OTHER** - Outros

---

## 🤝 Contribuindo

Contribuições são bem-vindas! Por favor, siga estas etapas:

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

### Diretrizes

- Mantenha o código limpo e legível
- Adicione testes para novas funcionalidades
- Atualize a documentação quando necessário
- Siga os padrões de código do projeto

---

## 👨‍💻 Autor

**Seu Nome**
- GitHub: [@seu-usuario](https://github.com/seu-usuario)
- LinkedIn: [Seu Nome](https://linkedin.com/in/seu-perfil)

---

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## 📞 Suporte

Para suporte, envie um email para: suporte@organizadorfinanceiro.com

Ou abra uma [issue no GitHub](https://github.com/seu-usuario/Backend---Organizador-Financeiro/issues).

---

## 🙏 Agradecimentos

- Spring Framework Team
- MySQL Community
- Todos os contribuidores do projeto

---

<div align="center">

**Desenvolvido com ❤️ usando Spring Boot**

⭐ Se este projeto te ajudou, considere dar uma estrela!

</div>
