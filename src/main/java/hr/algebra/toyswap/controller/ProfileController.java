package hr.algebra.toyswap.controller;

import hr.algebra.toyswap.dto.CreateCardDto;
import hr.algebra.toyswap.dto.MessageDto;
import hr.algebra.toyswap.repository.CreditCardRepository;
import hr.algebra.toyswap.repository.UserRepository;
import hr.algebra.toyswap.service.ProfileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

  private final UserRepository userRepository;
  private final CreditCardRepository creditCardRepository;
  private final ProfileService profileService;

  @GetMapping("/{id}")
  public String getProfile(@PathVariable("id") Long id, Model model) {
    final var user =
        userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    final var cards = creditCardRepository.findAllByUser(user);

    model.addAttribute("profile", user);
    model.addAttribute("cards", cards);
    model.addAttribute("newMessage", MessageDto.builder().build());

    return "profile";
  }

  @GetMapping("/image/{id}")
  public void getProfileImage(@PathVariable("id") Long id, HttpServletResponse response)
      throws IOException {
    profileService.getImage(id, response);
  }

  @GetMapping("/card/new")
  public String getCreateCard(Model model) {
    model.addAttribute("createCard", CreateCardDto.builder().build());
    return "create-card";
  }

  @PostMapping("/card/new")
  public String createCard(
      @Valid @ModelAttribute("createCard") CreateCardDto createCardDto,
      BindingResult bindingResult,
      Principal principal) {
    if (bindingResult.hasErrors()) {
      return "create-card";
    }
    profileService.createCreditCard(createCardDto, principal);

    final var user =
        userRepository
            .findByEmail(principal.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));

    return "redirect:/profile/" + user.getId();
  }

  @PostMapping("/credits/{value}")
  public String credits(
      @PathVariable("value") Long value, Principal principal, HttpServletRequest request) {
    final var user =
        userRepository
            .findByEmail(principal.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));

    profileService.addCredits(value, principal, request);

    return "redirect:/profile/" + user.getId();
  }

  @PostMapping("/card/delete/{id}")
  public String deleteCard(@PathVariable("id") Long id, Principal principal) {
    profileService.deleteCreditCard(id);
    final var user =
        userRepository
            .findByEmail(principal.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));
    return "redirect:/profile/" + user.getId();
  }
}
