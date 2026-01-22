package finance.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finance.domain.dto.accounts.AccountResponseDTO;
import finance.domain.dto.accounts.AccountUpdateDTO;
import finance.services.ServiceAdmin;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class ControllerAdmin {

    private final ServiceAdmin serviceAdmin;

    public ControllerAdmin(ServiceAdmin serviceAdmin) {
        this.serviceAdmin = serviceAdmin;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> patchAccount(@PathVariable Long id, @RequestBody AccountUpdateDTO data) {
        AccountResponseDTO updatedAccount = serviceAdmin.patchAccount(id, data);
        return ResponseEntity.ok(updatedAccount);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id) {
        serviceAdmin.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/accounts")
    public ResponseEntity<Page<AccountResponseDTO>> getAllAccounts(
            @PageableDefault(size = 10, page = 0) Pageable pageable) {

        Page<AccountResponseDTO> accounts = serviceAdmin.getAllAccounts(pageable);
        return ResponseEntity.ok(accounts);
    }

}
