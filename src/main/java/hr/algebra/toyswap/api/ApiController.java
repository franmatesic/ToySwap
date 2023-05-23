package hr.algebra.toyswap.api;

import hr.algebra.toyswap.converter.PostConverter;
import hr.algebra.toyswap.converter.TagConverter;
import hr.algebra.toyswap.dto.*;
import hr.algebra.toyswap.repository.PostRepository;
import hr.algebra.toyswap.repository.TagRepository;
import hr.algebra.toyswap.service.ApiAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    private final ApiAuthService apiAuthService;

    private final PostConverter postConverter;
    private final TagConverter tagConverter;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginDto loginDto) {
        final var body = apiAuthService.login(loginDto);
        return ResponseEntity.ok(body);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterDto registerDto) throws IOException {
        apiAuthService.register(registerDto);
        return ResponseEntity.ok("User registered successfully!");
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostDto>> getPosts() {
        final var posts = postRepository.findAllByDeactivatedAtIsNull();
        final var body = posts.stream().map(postConverter::convert).toList();
        return ResponseEntity.ok(body);
    }

    @GetMapping("/tags")
    public ResponseEntity<List<TagDto>> getTags() {
        final var tags = tagRepository.findAll();
        final var body = tags.stream().map(tagConverter::convert).toList();
        return ResponseEntity.ok(body);
    }
}
