package hr.algebra.toyswap.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class RegisterDto {

  @NotBlank(message = "Email je obavezan")
  @Email(message = "Unesite validan email")
  private String email;

  @NotBlank(message = "Lozinka je obavezna")
  @Length(min = 3, message = "Lozinka mora biti duža od 3 znaka")
  private String password;

  @NotBlank(message = "Ime je obavezno")
  private String firstName;

  @NotBlank(message = "Prezime je obavezno")
  private String lastName;

  @NotBlank(message = "Broj telefona je obavezan")
  @Pattern(
      regexp = "^[+]?[(]?[0-9]{3}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}$",
      message = "Unesite validan broj telefona")
  private String phoneNumber;

  private MultipartFile profilePicture;
}
