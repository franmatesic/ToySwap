package hr.algebra.toyswap.controller;

import hr.algebra.toyswap.dto.CreatePostDto;
import hr.algebra.toyswap.dto.MessageDto;
import hr.algebra.toyswap.model.post.Post;
import hr.algebra.toyswap.model.post.PostImage;
import hr.algebra.toyswap.repository.PostImageRepository;
import hr.algebra.toyswap.repository.PostRepository;
import hr.algebra.toyswap.repository.TagRepository;
import hr.algebra.toyswap.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Objects;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    @GetMapping("/{id}")
    public String getPost(@PathVariable("id") Long id, Model model) {
        final var post =
                postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        model.addAttribute("post", post);
        model.addAttribute("newMessage", MessageDto.builder().build());
        return "post";
    }

    @GetMapping("/new")
    public String getNewPost(Model model) {
        model.addAttribute("createPost", CreatePostDto.builder().build());
        model.addAttribute("tags", tagRepository.findAll());
        return "create-post";
    }

    @PostMapping("/new")
    public String newPost(
            @Valid @ModelAttribute("createPost") CreatePostDto createPostDto,
            BindingResult result,
            Principal principal) {
        if (result.hasErrors()) {
            return "create-post";
        }

        final var user =
                userRepository
                        .findByEmail(principal.getName())
                        .orElseThrow(() -> new RuntimeException("User not found"));

        final var tags = createPostDto.getTags().stream()
                .map(tag -> tagRepository.findById(tag).orElseThrow(() -> new RuntimeException("Tag not found")))
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

        return "redirect:/home";
    }

    @PostMapping("/buy/{id}")
    public String buy(@PathVariable("id") Long id, HttpServletRequest request, Principal principal) {
        if (Objects.isNull(principal)) {
            return "redirect:/login";
        }
        return "post";
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
