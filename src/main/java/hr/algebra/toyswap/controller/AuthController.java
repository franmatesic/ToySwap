package hr.algebra.toyswap.controller;

import hr.algebra.toyswap.dto.RegisterDto;
import hr.algebra.toyswap.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @GetMapping("/login")
  public String getLogin(Principal principal) {
    if (Objects.nonNull(principal)) {
      return "redirect:/home";
    }
    return "login";
  }

  @GetMapping("/register")
  public String getRegister(Model model, Principal principal) {
    if (Objects.nonNull(principal)) {
      return "redirect:/home";
    }
    model.addAttribute("register", RegisterDto.builder().build());
    return "register";
  }

  @PostMapping("/register")
  public String register(
      @Valid @ModelAttribute("register") RegisterDto registerDto,
      BindingResult result,
      HttpServletRequest request,
      Model model)
      throws IOException {
    model.addAttribute("register", registerDto);
    if (result.hasErrors()) {
      return "register";
    }
    authService.register(registerDto, request);

    return "redirect:/home";
  }

  @PostMapping("/logout")
  public String logout(HttpServletRequest request) {
    authService.logout(request);
    return "redirect:/home";
  }
}
