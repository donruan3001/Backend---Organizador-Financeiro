package finance.domain.transactions;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import finance.domain.acounts.Account;
import jakarta.persistence.*;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Enumerated(EnumType.STRING)
    private CategoryTransactions category;

    private String name;

    @Enumerated(EnumType.STRING)
    private TypeTransaction type;

    private BigDecimal amount;

    private LocalDateTime created;
    private LocalDateTime updated;

    public Transaction() {
    }

    public Transaction(Long id, Account account, CategoryTransactions category, String name,
            TypeTransaction type, BigDecimal amount, LocalDateTime created, LocalDateTime updated) {
        this.id = id;
        this.account = account;
        this.category = category;
        this.name = name;
        this.type = type;
        this.amount = amount;
        this.created = created;
        this.updated = updated;
    }

    public Transaction(Account account, CategoryTransactions category, String name,
            TypeTransaction type, BigDecimal amount) {
        this.account = account;
        this.category = category;
        this.name = name;
        this.type = type;
        this.amount = amount;
        this.created = LocalDateTime.now();
        this.updated = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public CategoryTransactions getCategory() {
        return category;
    }

    public void setCategory(CategoryTransactions category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeTransaction getType() {
        return type;
    }

    public void setType(TypeTransaction type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }
}
