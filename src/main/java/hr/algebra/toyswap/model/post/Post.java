package hr.algebra.toyswap.model.post;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hr.algebra.toyswap.model.user.User;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "post")
public class Post {

  public static final DecimalFormat priceFormat = new DecimalFormat("#,##0.00");

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "title")
  private String title;

  @Column(name = "description")
  private String description;

  @Column(name = "price", scale = 2)
  private BigDecimal price;

  @Column(name = "condition")
  @Enumerated(EnumType.STRING)
  private Condition condition;

  @Column(name = "created_at")
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @Column(name = "deactivated_at")
  private LocalDateTime deactivatedAt;

  @ManyToOne
  @JoinColumn(name = "buyer_id")
  private User buyer;

  @JsonIgnore
  @ManyToMany
  @JoinTable(
      name = "post_tag",
      joinColumns = @JoinColumn(name = "post_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id"))
  private List<Tag> tags;

  @JsonIgnore
  @OneToMany(mappedBy = "post")
  private List<PostImage> images;

  public String getFormattedPrice() {
    return priceFormat.format(price).concat(" €");
  }
}
