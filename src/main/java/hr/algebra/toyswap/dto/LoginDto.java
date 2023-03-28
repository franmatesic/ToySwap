package hr.algebra.toyswap.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginDto {

  @NotNull
  @Email
  private String email;

  @NotNull
  @Size(min = 3)
  private String password;
}
