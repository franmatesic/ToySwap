package hr.algebra.toyswap.model.user;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "credit_card")
public class CreditCard {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "number")
  private String number;

  @Column(name = "code")
  private String code;

  @Column(name = "name")
  private String name;

  @Column(name = "expiration_date")
  private String expirationDate;

  @Column(name = "created_at")
  @CreationTimestamp
  private LocalDateTime createdAt;

  public String getMaskedNumber() {
    return maskDigits(number);
  }

  private String maskDigits(final String input) {
    final var last = input.substring(12);
    return String.format("%12s", last).replace(" ", "*");
  }
}
