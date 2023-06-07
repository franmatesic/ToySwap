package hr.algebra.toyswap.model.user;

import hr.algebra.toyswap.model.post.Post;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "app_user")
public class User {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "email")
  private String email;

  @Column(name = "password")
  private String password;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "role")
  @Enumerated(EnumType.STRING)
  private UserRole role = UserRole.USER;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "currency", columnDefinition = "decimal(8,2) default 0")
  private BigDecimal currency = BigDecimal.ZERO;

  @Column(name = "enabled", columnDefinition = "boolean default true")
  private boolean enabled = true;

  @Column(name = "profile_picture")
  private byte[] profilePicture;

  @OneToMany(mappedBy = "user")
  private List<Post> posts;

  @OneToMany(mappedBy = "buyer")
  private List<Post> boughtPosts;

  public String getCurrencyFormatted() {
    return currency.toString() + "€";
  }
}
