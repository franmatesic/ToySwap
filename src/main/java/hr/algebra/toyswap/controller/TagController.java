package hr.algebra.toyswap.controller;

import hr.algebra.toyswap.dto.CreateTagDto;
import hr.algebra.toyswap.model.post.Tag;
import hr.algebra.toyswap.repository.TagRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tag")
@RequiredArgsConstructor
public class TagController {

  private final TagRepository tagRepository;

  @PostMapping
  public String createTag(
      @Valid @ModelAttribute("newTag") CreateTagDto createTagDto,
      BindingResult result,
      Model model) {
    model.addAttribute("newTag", createTagDto);
    if (result.hasErrors()) {
      return "redirect:/admin";
    }

    final var tag = Tag.builder().name(createTagDto.getName()).build();
    tagRepository.save(tag);

    return "redirect:/admin";
  }

  @PostMapping("/delete/{id}")
  public String deleteTag(@PathVariable("id") Long id) {
    tagRepository.deleteById(id);
    return "redirect:/admin";
  }
}
