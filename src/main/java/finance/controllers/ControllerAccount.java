package finance.controllers;

import java.util.List;

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

import finance.domain.dto.accounts.AccountCreateDTO;
import finance.domain.dto.accounts.AccountResponseDTO;
import finance.domain.dto.accounts.AccountUpdateDTO;
import finance.services.ServiceAccount;
import jakarta.validation.Valid;
@RestController
@RequestMapping("/accounts")
public class ControllerAccount {

    private final ServiceAccount serviceAccount;

    public ControllerAccount(ServiceAccount serviceAccount) {
        this.serviceAccount = serviceAccount;
    }


    @PostMapping
    public ResponseEntity<AccountResponseDTO> createAccount(@Valid @RequestBody AccountCreateDTO data) {
        var accounts = serviceAccount.createAccount(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(accounts);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponseDTO>> getAllAccontsUser() {
        return ResponseEntity.ok(serviceAccount.getAllAccountsUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> getAccountById(@PathVariable Long id) {
        AccountResponseDTO account = serviceAccount.getAccountById(id);
        return ResponseEntity.ok(account);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> updateAccount(
            @PathVariable Long id,
            @RequestBody @Valid AccountUpdateDTO data) {
        AccountResponseDTO updated = serviceAccount.updateAccount(id, data);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        serviceAccount.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

}
