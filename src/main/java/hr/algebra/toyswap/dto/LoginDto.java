package hr.algebra.toyswap.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
public class LoginDto {

    @NotBlank(message = "Email je obavezan")
    @Email(message = "Unesite validan email")
    private String email;

    @NotBlank(message = "Lozinka je obavezna")
    @Length(min = 3, message = "Lozinka mora biti duža od 3 znaka")
    private String password;
}
