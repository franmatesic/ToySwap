package hr.algebra.toyswap;

import static org.junit.jupiter.api.Assertions.assertEquals;

import hr.algebra.toyswap.converter.PostConverter;
import hr.algebra.toyswap.dto.PostDto;
import hr.algebra.toyswap.dto.TagDto;
import hr.algebra.toyswap.model.post.Condition;
import hr.algebra.toyswap.model.post.Post;
import hr.algebra.toyswap.model.post.Tag;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostConverterTest {

  @Autowired private PostConverter postConverter;

  @Test
  void shouldConvertEntityToDto() {
    final var entity = buildPost();
    final var expected = buildPostDto();

    final var result = postConverter.convert(entity);
    assertEquals(result, expected);
  }

  private Post buildPost() {
    final var tags =
        List.of(
            Tag.builder().id(1L).name("Tag 1").build(), Tag.builder().id(2L).name("Tag 2").build());
    return Post.builder()
        .id(1L)
        .title("Test title")
        .description("Test description")
        .price(BigDecimal.TEN)
        .condition(Condition.SHORT_USED)
        .tags(tags)
        .build();
  }

  private PostDto buildPostDto() {
    final var tags =
        List.of(
            TagDto.builder().id(1L).name("Tag 1").build(),
            TagDto.builder().id(2L).name("Tag 2").build());
    return PostDto.builder()
        .id(1L)
        .title("Test title")
        .description("Test description")
        .price(BigDecimal.TEN)
        .condition(Condition.SHORT_USED)
        .tags(tags)
        .build();
  }
}
