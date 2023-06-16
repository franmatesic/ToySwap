package hr.algebra.toyswap.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordDto {

  @NotBlank(message = "Email je obavezan")
  @Email(message = "Unesite validan email")
  private String email;
}
