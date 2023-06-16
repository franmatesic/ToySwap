package hr.algebra.toyswap.controller;

import hr.algebra.toyswap.dto.ForgotPasswordDto;
import hr.algebra.toyswap.dto.LoginDto;
import hr.algebra.toyswap.dto.RegisterDto;
import hr.algebra.toyswap.dto.ResetPasswordDto;
import hr.algebra.toyswap.exception.AuthException;
import hr.algebra.toyswap.model.user.User;
import hr.algebra.toyswap.repository.UserRepository;
import hr.algebra.toyswap.service.AuthService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final UserRepository userRepository;
  private final JavaMailSender mailSender;

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

  @GetMapping("/forgotPassword")
  public String getForgotPassword(Model model, Principal principal) {
    if (Objects.nonNull(principal)) {
      return "redirect:/home";
    }
    model.addAttribute("email", new ForgotPasswordDto());
    return "forgot-password";
  }

  @GetMapping("/resetPassword/{token}")
  public String getResetPassword(
      @PathVariable("token") String token, Model model, Principal principal) {
    if (Objects.nonNull(principal)) {
      return "redirect:/home";
    }
    model.addAttribute("token", token);
    model.addAttribute("password", new ResetPasswordDto());
    return "reset-password";
  }

  @PostMapping("/login")
  public String login(
      @Valid @ModelAttribute("login") LoginDto loginDto,
      BindingResult result,
      HttpServletRequest request,
      Model model) {
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

  @PostMapping("/forgotPassword")
  public String forgotPassword(
      @Valid @ModelAttribute("email") ForgotPasswordDto forgotPasswordDto,
      BindingResult result,
      HttpServletRequest request,
      Model model)
      throws MessagingException, UnsupportedEncodingException {
    model.addAttribute("email", forgotPasswordDto);
    if (result.hasErrors()) {
      return "forgot-password";
    }

    final var user = userRepository.findByEmail(forgotPasswordDto.getEmail());
    if (user.isEmpty()) {
      result.addError(new ObjectError("email", "Korisnik s ovim Email-om ne postoji"));
      return "forgot-password";
    }

    final var token = UUID.randomUUID().toString();
    authService.updateResetPasswordToken(forgotPasswordDto.getEmail(), token);
    final var resetLink = getURL(request) + "/resetPassword/" + token;

    sendEmail(user.get(), resetLink);

    return "redirect:/home";
  }

  @PostMapping("/resetPassword/{token}")
  public String resetPassword(
      @PathVariable("token") String token,
      @Valid @ModelAttribute("password") ResetPasswordDto resetPasswordDto,
      BindingResult result,
      Model model) {
    model.addAttribute("password", resetPasswordDto);
    if (result.hasErrors()) {
      return "redirect:/resetPassword/" + token;
    }

    final var user =
        userRepository
            .findByResetPasswordToken(token)
            .orElseThrow(() -> new AuthException("User not found"));
    authService.updatePassword(user, resetPasswordDto.getPassword());

    return "redirect:/login";
  }

  @PostMapping("/logout")
  public String logout(HttpServletRequest request) {
    authService.logout(request);
    return "redirect:/home";
  }

  private String getURL(HttpServletRequest request) {
    return request.getRequestURL().toString().replace(request.getServletPath(), "");
  }

  private void sendEmail(final User recipient, final String link)
      throws MessagingException, UnsupportedEncodingException {

    final var message = mailSender.createMimeMessage();
    final var helper = new MimeMessageHelper(message);

    helper.setFrom("toyswap.mail@gmail.com", "Toyswap");
    helper.setTo(recipient.getEmail());

    helper.setSubject("Reset password");
    helper.setText(
        "<p>Hello,</p>"
            + "<p>You have requested to reset your password.</p>"
            + "<p>Click the link below to change your password:</p>"
            + "<p><a href=\""
            + link
            + "\">Change my password</a></p>"
            + "<br>"
            + "<p>Ignore this email if you do remember your password, "
            + "or you have not made the request.</p>",
        true);

    mailSender.send(message);
  }
}
