package hr.algebra.toyswap.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreditCardDto {

  private Long id;
  private String maskedNumber;
  private String name;
}
