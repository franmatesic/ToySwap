package hr.algebra.toyswap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import hr.algebra.toyswap.dto.CreatePostDto;
import hr.algebra.toyswap.model.post.Condition;
import hr.algebra.toyswap.model.post.Post;
import hr.algebra.toyswap.repository.PostRepository;
import hr.algebra.toyswap.service.PostService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostServiceTest {

  @Autowired private PostService postService;
  @Autowired private PostRepository postRepository;

  private Post testPost;

  @AfterAll
  public void cleanup() {
    if (testPost != null) {
      postRepository.delete(testPost);
    }
  }

  @Test
  @Order(1)
  void shouldCreatePost() {
    final var createPostDto = buildCreatePostDto();

    final var postsBefore = postRepository.count();
    testPost = postService.createPost(createPostDto, "admin@toyswap.com");
    final var postsAfter = postRepository.count();

    assertEquals(postsBefore + 1, postsAfter);
    assertEquals(testPost.getUser().getEmail(), "admin@toyswap.com");
    assertEquals(testPost.getTitle(), createPostDto.getTitle());
    assertEquals(testPost.getDescription(), createPostDto.getDescription());
    assertEquals(testPost.getPrice(), createPostDto.getPrice());
    assertEquals(testPost.getCondition(), createPostDto.getCondition());
  }

  @Test
  @Order(2)
  void shouldGetPost() {
    final var id = testPost.getId();

    final var post = postService.get(id);
    assertEquals(post.getUser().getId(), testPost.getUser().getId());
    assertEquals(post.getTitle(), testPost.getTitle());
    assertEquals(post.getDescription(), testPost.getDescription());
    assertEquals(post.getPrice().compareTo(testPost.getPrice()), 0);
    assertEquals(post.getCondition(), testPost.getCondition());
  }

  @Test
  @Order(3)
  void shouldDeletePost() {
    final var id = testPost.getId();

    postService.delete(id);
    final var postAfterDelete = postService.get(id);

    assertTrue(postAfterDelete.getDeactivatedAt() != null);
    assertTrue(postAfterDelete.getDeactivatedAt().isBefore(LocalDateTime.now()));
    assertTrue(postAfterDelete.getDeactivatedAt().isAfter(LocalDateTime.now().minusSeconds(1)));
  }

  private CreatePostDto buildCreatePostDto() {
    return CreatePostDto.builder()
        .title("Test title")
        .description("Test description")
        .price(BigDecimal.TEN)
        .condition(Condition.SHORT_USED)
        .tags(Collections.emptyList())
        .images(Collections.emptyList())
        .build();
  }
}
