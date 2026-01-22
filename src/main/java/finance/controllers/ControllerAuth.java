package finance.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finance.domain.dto.user.ResponseJwtDTO;
import finance.domain.dto.user.UserLoginDTO;
import finance.domain.dto.user.UserProfileDTO;
import finance.domain.dto.user.UserRegisterDTO;
import finance.services.ServiceAuth;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class ControllerAuth {

    private final ServiceAuth serviceAuth;

    public ControllerAuth(ServiceAuth serviceAuth) {
        this.serviceAuth = serviceAuth;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegisterDTO user) {
        serviceAuth.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseJwtDTO> login(@RequestBody @Valid UserLoginDTO user) {
        ResponseJwtDTO token = serviceAuth.login(user.email(), user.password());
        return ResponseEntity.ok(token);
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileDTO> getUserProfile() {
        UserProfileDTO profile = serviceAuth.getUserProfile();
        return ResponseEntity.ok(profile);
    }

}
