package hr.algebra.toyswap.controller;

import hr.algebra.toyswap.dto.LoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

  @GetMapping
  public String getLogin(Model model) {
    model.addAttribute("login", LoginDto.builder().build());
    return "login";
  }
}
