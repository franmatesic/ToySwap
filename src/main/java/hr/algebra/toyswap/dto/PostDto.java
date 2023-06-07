package hr.algebra.toyswap.dto;

import hr.algebra.toyswap.model.post.Condition;
import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostDto {

  private Long id;
  private String title;
  private String description;
  private Condition condition;
  private BigDecimal price;
  private List<TagDto> tags;
  private List<Long> imageIds;
}
