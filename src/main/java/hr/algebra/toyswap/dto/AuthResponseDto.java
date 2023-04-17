package hr.algebra.toyswap.dto;

import hr.algebra.toyswap.model.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AuthResponseDto {

  private String token;
  private String email;
  private String firstName;
  private String lastName;
  private UserRole role;
  private String phoneNumber;
  private byte[] profilePicture;
}
