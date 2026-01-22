package finance.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finance.domain.dto.transactions.TransactionCreateDTO;
import finance.domain.dto.transactions.TransactionResponseDTO;
import finance.domain.dto.transactions.TransactionUpdateDTO;
import finance.services.ServiceTransactions;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class ControllerTransactions {

    private final ServiceTransactions serviceTransactions;
    
    public ControllerTransactions(ServiceTransactions serviceTransactions) {
        this.serviceTransactions = serviceTransactions;
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(@RequestBody @Valid TransactionCreateDTO data) {
        TransactionResponseDTO response = serviceTransactions.createTransaction(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions() {
        List<TransactionResponseDTO> transactions = serviceTransactions.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransactionById(@PathVariable Long id) {
        TransactionResponseDTO transaction = serviceTransactions.getTransactionById(id);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByAccount(@PathVariable Long accountId) {
        List<TransactionResponseDTO> transactions = serviceTransactions.getTransactionsByAccountId(accountId);
        return ResponseEntity.ok(transactions);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> updateTransaction(
            @PathVariable Long id,
            @RequestBody @Valid TransactionUpdateDTO data) {
        TransactionResponseDTO updated = serviceTransactions.updateTransaction(id, data);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        serviceTransactions.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
