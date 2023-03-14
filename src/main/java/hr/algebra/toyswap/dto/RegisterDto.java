package hr.algebra.toyswap.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterDto {

  private String email;
  private String password;
  private String firstName;
  private String lastName;
  private String phoneNumber;
  private byte[] profilePicture;
}
