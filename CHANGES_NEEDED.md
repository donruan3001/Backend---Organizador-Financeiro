# Mudanças Pendentes no Código

## 🔴 Críticas (Devem ser resolvidas antes de produção)

### 1. Corrigir erros de compilação
**Localização:**
- `ServiceAccount.java:40` e `ServiceAccount.java:55`
- `ServiceAuth.java:87`

**Problema:** Tentando passar `Long` onde espera-se `String` nas exceções

**Solução:** Adicionar sobrecarga de construtores nas exceções para aceitar Long:
```java
// Em UserNotFoundException.java - já adicionado
public UserNotFoundException(Long userId) {
    super("Usuário com ID " + userId + " não encontrado");
}
```

### 2. Melhorar mensagens de erro de autenticação
**Localização:** `AuthenticatedUser.java:14`

**Problema:** Lança `RuntimeException` genérica

**Solução:** Criar e lançar exceção específica de segurança:
```java
throw new AuthenticationException("Usuário não autenticado");
```

### 3. Adicionar índices no banco de dados
**Problema:** Queries podem ser lentas sem índices apropriados

**Solução:** Adicionar anotações nas entidades:
```java
@Table(name = "transactions", indexes = {
    @Index(name = "idx_account_id", columnList = "account_id"),
    @Index(name = "idx_created", columnList = "created")
})
```

---

## 🟡 Importantes (Melhoram qualidade e segurança)

### 4. Adicionar paginação em transações
**Localização:** `ServiceTransactions.getAllTransactions()`

**Problema:** Pode retornar milhares de registros sem controle

**Solução:** Adicionar parâmetro Pageable:
```java
public Page<TransactionResponseDTO> getAllTransactions(Pageable pageable) {
    // implementação com paginação
}
```

### 5. Validar entrada em TransactionCreateDTO
**Localização:** `TransactionCreateDTO.java`

**Problema:** Campo `name` e `category` não têm validação

**Solução:**
```java
@NotBlank(message = "Nome é obrigatório")
String name,

@NotNull(message = "Categoria é obrigatória")
CategoryTransactions category,
```

### 6. Adicionar auditoria
**Problema:** Não há rastreamento de quem/quando modificou registros

**Solução:** Adicionar campos de auditoria nas entidades:
```java
@CreatedDate
private LocalDateTime createdAt;

@LastModifiedDate
private LocalDateTime updatedAt;

@CreatedBy
private String createdBy;

@LastModifiedBy
private String lastModifiedBy;
```

### 7. Implementar soft delete
**Problema:** DELETE físico remove dados permanentemente

**Solução:** Adicionar flag de exclusão lógica:
```java
@Column(name = "deleted")
private boolean deleted = false;

@Column(name = "deleted_at")
private LocalDateTime deletedAt;
```

### 8. Adicionar rate limiting
**Problema:** Endpoints de autenticação vulneráveis a ataques de força bruta

**Solução:** Implementar rate limiting com Bucket4j ou similar

---

## 🟢 Melhorias Opcionais (Nice to have)

### 9. Adicionar cache
**Localização:** Métodos de leitura frequentes

**Solução:** Usar Spring Cache:
```java
@Cacheable(value = "accounts", key = "#userId")
public List<AccountResponseDTO> getAllAccountsUser() {
    // ...
}
```

### 10. Adicionar filtros e ordenação
**Endpoints:** GET de transações e contas

**Solução:** Implementar Specification pattern ou usar QueryDSL

### 11. Adicionar testes unitários e integração
**Problema:** Não há cobertura de testes

**Solução:** Criar testes para:
- Services (com Mockito)
- Controllers (com MockMvc)
- Repositories (com @DataJpaTest)

### 12. Implementar refresh token
**Localização:** Sistema de autenticação

**Benefício:** Melhor experiência do usuário, tokens de curta duração mais seguros

### 13. Adicionar documentação Swagger completa
**Problema:** Endpoints podem não estar bem documentados

**Solução:** Adicionar anotações @Operation, @ApiResponse, etc.

### 14. Implementar backup automático
**Problema:** Sem estratégia de backup

**Solução:** Configurar rotina de backup do banco de dados

### 15. Adicionar logs estruturados
**Problema:** Logs podem ser difíceis de rastrear

**Solução:** Usar SLF4J com Logback e formato JSON:
```java
log.info("Transaction created",
    kv("transactionId", transaction.getId()),
    kv("accountId", account.getId()),
    kv("amount", transaction.getAmount())
);
```

### 16. Implementar health checks
**Solução:** Adicionar Spring Actuator:
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
```

### 17. Adicionar métricas e monitoramento
**Solução:** Integrar com Prometheus + Grafana

### 18. Implementar HATEOAS
**Benefício:** API mais RESTful com links de navegação

### 19. Adicionar suporte a múltiplas moedas
**Localização:** Entidade Account e Transaction

**Solução:** Adicionar campo currency:
```java
@Column(name = "currency")
private String currency = "BRL";
```

### 20. Implementar notificações
**Funcionalidade:** Enviar emails ou push notifications para eventos importantes
- Transação criada
- Saldo baixo
- Transação suspeita

---

## 📋 Refatorações Recomendadas

### 21. Extrair lógica de negócio do Service de transações
**Problema:** Método `createTransaction` e `updateTransaction` têm lógica duplicada de atualização de saldo

**Solução:** Criar classe `AccountBalanceManager`:
```java
public class AccountBalanceManager {
    public void applyTransaction(Account account, Transaction transaction) {
        // lógica centralizada
    }

    public void revertTransaction(Account account, Transaction transaction) {
        // lógica centralizada
    }
}
```

### 22. Criar DTOs de resposta padronizados
**Problema:** Respostas de sucesso/erro não são consistentes

**Solução:** Criar wrapper padrão:
```java
public record ApiResponse<T>(
    boolean success,
    String message,
    T data,
    LocalDateTime timestamp
) {}
```

### 23. Separar configurações por ambiente
**Problema:** application.properties único para todos os ambientes

**Solução:** Criar:
- application-dev.properties
- application-test.properties
- application-prod.properties

### 24. Melhorar tratamento de exceções global
**Localização:** `GlobalControllerExceptionHandler`

**Solução:** Adicionar handlers para mais tipos de exceção:
```java
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
    // ...
}
```

---

## 🔒 Segurança

### 25. Adicionar proteção CSRF para formulários web
**Problema:** Se houver frontend web, vulnerável a CSRF

**Solução:** Habilitar CSRF no Spring Security para endpoints stateful

### 26. Implementar helmet headers
**Solução:** Adicionar headers de segurança:
```java
http.headers()
    .xssProtection()
    .contentSecurityPolicy("default-src 'self'")
    .frameOptions().deny();
```

### 27. Adicionar validação de força de senha
**Localização:** Registro de usuário

**Solução:** Implementar validador customizado

### 28. Implementar bloqueio de conta após tentativas falhas
**Localização:** Sistema de autenticação

---

## 🗄️ Banco de Dados

### 29. Revisar estratégia de migration
**Problema:** Flyway configurado mas migrations podem estar inconsistentes

**Ação:** Revisar e consolidar migrations V1, V2

### 30. Adicionar constraints de banco
**Solução:** Garantir integridade referencial e constraints:
```sql
ALTER TABLE accounts ADD CONSTRAINT check_balance_positive
CHECK (balance >= 0);
```

---

## 📊 Relatórios e Analytics (Futuro)

### 31. Endpoint de resumo financeiro
```java
GET /api/accounts/{id}/summary
- Retorna: total receitas, total despesas, saldo atual, período
```

### 32. Endpoint de transações por categoria
```java
GET /api/transactions/by-category?startDate=...&endDate=...
```

### 33. Endpoint de gráfico de evolução de saldo
```java
GET /api/accounts/{id}/balance-history?period=30days
```

### 34. Exportar transações para CSV/Excel
```java
GET /api/transactions/export?format=csv&startDate=...&endDate=...
```

---

## Priorização Sugerida

### Sprint 1 (Urgente - 1 semana)
1. Corrigir erros de compilação ✓ (em andamento)
2. Adicionar validações faltantes
3. Adicionar índices no banco
4. Implementar paginação

### Sprint 2 (Importante - 2 semanas)
5. Adicionar testes unitários
6. Implementar auditoria
7. Melhorar tratamento de exceções
8. Adicionar logs estruturados

### Sprint 3 (Melhorias - 2 semanas)
9. Implementar cache
10. Adicionar filtros e ordenação
11. Implementar soft delete
12. Adicionar health checks

### Sprint 4 (Features - 3 semanas)
13. Implementar refresh token
14. Adicionar relatórios financeiros
15. Implementar notificações
16. Adicionar exportação de dados

---

## Notas Técnicas

### Performance
- Considerar usar índices compostos para queries complexas
- Implementar eager/lazy loading strategy adequada
- Avaliar uso de projeções para queries pesadas

### Escalabilidade
- Considerar separação de leitura/escrita (CQRS pattern)
- Implementar message queue para operações assíncronas
- Adicionar Redis para cache distribuído

### Observabilidade
- Implementar tracing distribuído (Zipkin/Jaeger)
- Adicionar logging de todas as operações críticas
- Criar dashboards de monitoramento

### DevOps
- Criar Dockerfile para containerização
- Configurar CI/CD pipeline
- Implementar deploy automatizado
