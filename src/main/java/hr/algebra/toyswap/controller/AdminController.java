package hr.algebra.toyswap.controller;

import hr.algebra.toyswap.dto.CreateTagDto;
import hr.algebra.toyswap.repository.PostRepository;
import hr.algebra.toyswap.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

  private final PostRepository postRepository;
  private final TagRepository tagRepository;

  @GetMapping
  public String getAdmin(Model model) {
    model.addAttribute("posts", postRepository.findAll());
    model.addAttribute("tags", tagRepository.findAll());
    model.addAttribute("newTag", CreateTagDto.builder().build());
    return "admin";
  }
}
