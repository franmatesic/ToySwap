package hr.algebra.toyswap.controller;

import hr.algebra.toyswap.dto.PostDto;
import hr.algebra.toyswap.model.post.Post;
import hr.algebra.toyswap.model.post.PostImage;
import hr.algebra.toyswap.repository.PostImageRepository;
import hr.algebra.toyswap.repository.PostRepository;
import hr.algebra.toyswap.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

  private final PostRepository postRepository;
  private final PostImageRepository postImageRepository;
  private final UserRepository userRepository;

  @GetMapping("/{id}")
  public String getPost(@PathVariable("id") Long id, Model model) {
    final var post =
        postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
    model.addAttribute("post", post);
    return "post";
  }

  @PostMapping("/create")
  public String createPost(
      @Valid @ModelAttribute("createPost") PostDto postDto,
      BindingResult result,
      Principal principal) {
    if (result.hasErrors()) {
      return "fragments/create-post";
    }

    final var user =
        userRepository
            .findByEmail(principal.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));

    final var post =
        Post.builder()
            .user(user)
            .title(postDto.getTitle())
            .description(postDto.getDescription())
            .condition(postDto.getCondition())
            .price(postDto.getPrice())
            .tags(postDto.getTags())
            .build();
    final var savedPost = postRepository.save(post);

    for (var file : postDto.getImages()) {
      try {
        final var bytes = file.getBytes();
        final var postImage = PostImage.builder().image(bytes).post(savedPost).build();
        postImageRepository.save(postImage);

      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    return "redirect:/home";
  }

  @GetMapping("/image/{id}")
  public void getImage(@PathVariable("id") Long id, HttpServletResponse response)
      throws IOException {
    response.setContentType("image/png");
    final var image =
        postImageRepository.findById(id).orElseThrow(() -> new RuntimeException("Image not found"));

    final var is = new ByteArrayInputStream(image.getImage());
    IOUtils.copy(is, response.getOutputStream());
  }
}
