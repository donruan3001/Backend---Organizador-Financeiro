package finance.domain.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRegisterDTO(

        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank String password) {

}