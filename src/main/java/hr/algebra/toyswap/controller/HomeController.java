package hr.algebra.toyswap.controller;

import hr.algebra.toyswap.dto.CreatePostDto;
import hr.algebra.toyswap.repository.PostRepository;
import hr.algebra.toyswap.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/", "/home"})
@RequiredArgsConstructor
public class HomeController {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    @GetMapping
    public String getHome(Model model) {
        model.addAttribute("posts", postRepository.findAllByDeactivatedAtIsNull());
        model.addAttribute("tags", tagRepository.findAll());
        model.addAttribute("createPost", CreatePostDto.builder().build());
        return "home";
    }
}
