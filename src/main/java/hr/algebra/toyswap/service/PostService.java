package hr.algebra.toyswap.service;

import hr.algebra.toyswap.auth.UserDetailsImpl;
import hr.algebra.toyswap.dto.CreatePostDto;
import hr.algebra.toyswap.model.Message;
import hr.algebra.toyswap.model.post.Post;
import hr.algebra.toyswap.model.post.PostImage;
import hr.algebra.toyswap.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final PostImageRepository postImageRepository;
  private final MessageRepository messageRepository;
  private final TagRepository tagRepository;
  private final UserRepository userRepository;
  private final AuthService authService;

  public void createPost(final CreatePostDto createPostDto, final Principal principal) {
    final var user =
        userRepository
            .findByEmail(principal.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));

    final var tags =
        createPostDto.getTags().stream()
            .map(
                tag ->
                    tagRepository
                        .findById(tag)
                        .orElseThrow(() -> new RuntimeException("Tag not found")))
            .toList();

    final var post =
        Post.builder()
            .user(user)
            .title(createPostDto.getTitle())
            .description(createPostDto.getDescription())
            .condition(createPostDto.getCondition())
            .price(createPostDto.getPrice())
            .tags(tags)
            .build();
    final var savedPost = postRepository.save(post);

    for (var file : createPostDto.getImages()) {
      try {
        final var bytes = file.getBytes();
        final var postImage = PostImage.builder().image(bytes).post(savedPost).build();
        postImageRepository.save(postImage);

      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public void delete(final Long id) {
    final var post =
        postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
    post.setDeactivatedAt(LocalDateTime.now());
    postRepository.save(post);
  }

  public void getImage(final Long id, final HttpServletResponse response) throws IOException {
    response.setContentType("image/png");
    final var image =
        postImageRepository.findById(id).orElseThrow(() -> new RuntimeException("Image not found"));

    final var is = new ByteArrayInputStream(image.getImage());
    IOUtils.copy(is, response.getOutputStream());
  }

  public String buyPost(
      final Long id, final HttpServletRequest request, final Principal principal) {
    final var user =
        userRepository
            .findByEmail(principal.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));
    final var post =
        postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));

    if (user.getCurrency().compareTo(post.getPrice()) < 0) {
      return "redirect:/post/" + id;
    }

    user.setCurrency(user.getCurrency().subtract(post.getPrice()));
    userRepository.save(user);
    authService.update(UserDetailsImpl.build(user), request);

    final var poster = post.getUser();
    poster.setCurrency(poster.getCurrency().add(post.getPrice()));
    userRepository.save(poster);

    post.setDeactivatedAt(LocalDateTime.now());
    post.setBuyer(user);
    postRepository.save(post);

    final var formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    final var content =
        String.format(
            "Korisnik %s %s kupio je %s %s za %s€",
            user.getFirstName(),
            user.getLastName(),
            post.getTitle(),
            post.getDeactivatedAt().format(formatter),
            post.getPrice());
    final var message =
        Message.builder().sender(user).receiver(post.getUser()).content(content).build();
    messageRepository.save(message);

    return "redirect:/messages/" + post.getUser().getId();
  }
}
