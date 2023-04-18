package hr.algebra.toyswap.controller;

import hr.algebra.toyswap.dto.LoginDto;
import hr.algebra.toyswap.dto.RegisterDto;
import hr.algebra.toyswap.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.security.Principal;
import java.util.Objects;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String getLogin(Model model, Principal principal) {
        if (Objects.nonNull(principal)) {
            return "redirect:/home";
        }
        model.addAttribute("login", LoginDto.builder().build());
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

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("login") LoginDto loginDto, BindingResult result, HttpServletRequest request, Model model) {
        model.addAttribute("login", loginDto);
        if (result.hasErrors()) {
            return "login";
        }
        return authService.login(loginDto, result, request);
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
        return authService.register(registerDto, result, request);
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        authService.logout(request);
        return "redirect:/home";
    }
}
