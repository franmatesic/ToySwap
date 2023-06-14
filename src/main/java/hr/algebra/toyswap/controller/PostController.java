package hr.algebra.toyswap.controller;

import hr.algebra.toyswap.dto.CreatePostDto;
import hr.algebra.toyswap.dto.MessageDto;
import hr.algebra.toyswap.repository.*;
import hr.algebra.toyswap.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

  private final TagRepository tagRepository;
  private final PostService postService;

  @GetMapping("/{id}")
  public String getPost(@PathVariable("id") Long id, Model model) {
    final var post = postService.get(id);
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
    postService.createPost(createPostDto, principal.getName());

    return "redirect:/home";
  }

  @PostMapping("/buy/{id}")
  public String buy(@PathVariable("id") Long id, HttpServletRequest request, Principal principal) {
    if (Objects.isNull(principal)) {
      return "redirect:/login";
    }
    return postService.buyPost(id, request, principal.getName());
  }

  @PostMapping("/delete/{id}")
  public String delete(@PathVariable("id") Long id) {
    postService.delete(id);
    return "redirect:/";
  }

  @GetMapping("/image/{id}")
  public void getImage(@PathVariable("id") Long id, HttpServletResponse response)
      throws IOException {
    postService.getImage(id, response);
  }
}
