package hr.algebra.toyswap.api;

import hr.algebra.toyswap.auth.UserDetailsImpl;
import hr.algebra.toyswap.converter.PostConverter;
import hr.algebra.toyswap.converter.TagConverter;
import hr.algebra.toyswap.dto.*;
import hr.algebra.toyswap.repository.*;
import hr.algebra.toyswap.service.ApiAuthService;
import hr.algebra.toyswap.service.PostService;
import hr.algebra.toyswap.service.ProfileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

  private final PostRepository postRepository;
  private final TagRepository tagRepository;
  private final UserRepository userRepository;
  private final CreditCardRepository creditCardRepository;
  private final PostImageRepository postImageRepository;

  private final PostService postService;
  private final ProfileService profileService;
  private final ApiAuthService apiAuthService;

  private final PostConverter postConverter;
  private final TagConverter tagConverter;

  @PostMapping("/login")
  public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginDto loginDto) {
    final var body = apiAuthService.login(loginDto);
    return ResponseEntity.ok(body);
  }

  @PostMapping("/register")
  public ResponseEntity<String> register(@Valid @RequestBody RegisterDto registerDto)
      throws IOException {
    apiAuthService.register(registerDto);
    return ResponseEntity.ok("User registered successfully!");
  }

  // Podaci o korisniku
  @GetMapping("/profile")
  public ResponseEntity<UserDetailsImpl> getProfile(Principal principal) {
    final var user =
        userRepository
            .findByEmail(principal.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));
    final var userDetails = UserDetailsImpl.build(user);
    return ResponseEntity.ok(userDetails);
  }

  // Profilna slika korisnika
  @GetMapping("/profile/image")
  public void getProfileImage(Principal principal, HttpServletResponse response)
      throws IOException {
    final var user =
        userRepository
            .findByEmail(principal.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));
    profileService.getImage(user.getId(), response);
  }

  @PostMapping("/card")
  public ResponseEntity<Void> createCard(
      @Valid @RequestBody CreateCardDto createCardDto, Principal principal) {
    profileService.createCreditCard(createCardDto, principal);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @DeleteMapping("/card/{id}")
  public ResponseEntity<Void> deleteCard(@PathVariable("id") Long id) {
    profileService.deleteCreditCard(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/cards")
  public ResponseEntity<List<CreditCardDto>> getCards(Principal principal) {
    final var user =
        userRepository
            .findByEmail(principal.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));
    final var cards = creditCardRepository.findAllByUser(user);
    return ResponseEntity.ok(
        cards.stream()
            .map(
                card ->
                    CreditCardDto.builder()
                        .id(card.getId())
                        .name(card.getName())
                        .maskedNumber(card.getMaskedNumber())
                        .build())
            .toList());
  }

  // Dodaj kredite na racun
  @PostMapping("/credits/{value}")
  public ResponseEntity<Void> addCredits(
      @PathVariable("value") Long value, Principal principal, HttpServletRequest request) {
    profileService.addCredits(value, principal, request);
    return ResponseEntity.ok().build();
  }

  // Sve korisnicke objave
  @GetMapping("/myPosts")
  public ResponseEntity<List<PostDto>> myPosts(Principal principal) {
    final var user =
        userRepository
            .findByEmail(principal.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));
    final var posts = user.getPosts().stream().map(postConverter::convert).toList();
    return ResponseEntity.ok(posts);
  }

  // Sve korisnicke kupnje
  @GetMapping("/myPurchases")
  public ResponseEntity<List<PostDto>> myPurchases(Principal principal) {
    final var user =
        userRepository
            .findByEmail(principal.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));
    final var posts = user.getBoughtPosts().stream().map(postConverter::convert).toList();
    return ResponseEntity.ok(posts);
  }

  @GetMapping("/tags")
  public ResponseEntity<List<TagDto>> getTags() {
    final var tags = tagRepository.findAll();
    final var body = tags.stream().map(tagConverter::convert).toList();
    return ResponseEntity.ok(body);
  }

  @GetMapping("/posts")
  public ResponseEntity<List<PostDto>> getPosts() {
    final var posts = postRepository.findAllByDeactivatedAtIsNull();
    final var body = posts.stream().map(postConverter::convert).toList();
    return ResponseEntity.ok(body);
  }

  // Dohvati jednu objavu
  @GetMapping("/post/{id}")
  public ResponseEntity<PostDto> getPost(@PathVariable("id") Long id) {
    final var post =
        postRepository
            .findById(id)
            .map(postConverter::convert)
            .orElseThrow(() -> new RuntimeException("Post not found"));
    return ResponseEntity.ok(post);
  }

  // Dohvati jednu sliku objave
  @GetMapping("/post/image/{id}")
  public void getPostImages(@PathVariable("id") Long id, HttpServletResponse response)
      throws IOException {
    postService.getImage(id, response);
  }

  // Napravi novu objavu
  @PostMapping("/post")
  public ResponseEntity<Void> createPost(
      @Valid @RequestBody CreatePostDto createPostDto, Principal principal) {
    postService.createPost(createPostDto, principal);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  // Obrisi objavu
  @DeleteMapping("/post/{id}")
  public ResponseEntity<Void> deletePost(@PathVariable("id") Long id) {
    postService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
