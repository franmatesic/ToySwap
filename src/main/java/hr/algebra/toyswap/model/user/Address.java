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
@Table(name = "address")
public class Address {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "country")
  private String country;

  @Column(name = "town")
  private String town;

  @Column(name = "postal_code")
  private String postalCode;

  @Column(name = "street")
  private String street;

  @Column(name = "created_at")
  @CreationTimestamp
  private LocalDateTime createdAt;
}
