package hr.algebra.toyswap.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ResetPasswordDto {

  @NotBlank(message = "Lozinka je obavezna")
  @Length(min = 3, message = "Lozinka mora biti duža od 3 znaka")
  private String password;
}
