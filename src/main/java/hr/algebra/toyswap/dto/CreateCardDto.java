package hr.algebra.toyswap.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
public class CreateCardDto {

  @NotBlank(message = "Broj je obavezan")
  @Length(min = 16, max = 16, message = "Broj mora biti dug 16 znakova")
  private String number;

  @NotBlank(message = "Ime je obavezno")
  @Length(min = 3, message = "Ime mora biti dugo barem 3 znakova")
  private String name;

  @NotBlank(message = "Kod je obavezan")
  @Length(min = 3, max = 3, message = "Kod mora biti dug 3 znakova")
  private String code;

  @NotBlank(message = "Datum roka trajanja je obavezan")
  private String expirationDate;
}
