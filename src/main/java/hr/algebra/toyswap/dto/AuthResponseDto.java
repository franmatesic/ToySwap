package hr.algebra.toyswap.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import hr.algebra.toyswap.auth.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponseDto {

  private String token;
  private UserDetailsImpl user;
  private String error;
}
