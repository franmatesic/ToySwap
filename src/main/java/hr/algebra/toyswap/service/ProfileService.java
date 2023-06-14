package hr.algebra.toyswap.service;

import hr.algebra.toyswap.auth.UserDetailsImpl;
import hr.algebra.toyswap.dto.CreateCardDto;
import hr.algebra.toyswap.model.user.CreditCard;
import hr.algebra.toyswap.repository.CreditCardRepository;
import hr.algebra.toyswap.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

  private final UserRepository userRepository;
  private final CreditCardRepository creditCardRepository;
  private final AuthService authService;

  public void createCreditCard(CreateCardDto createCardDto, Principal principal) {
    final var user =
        userRepository
            .findByEmail(principal.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));
    final var card =
        CreditCard.builder()
            .user(user)
            .name(createCardDto.getName())
            .number(createCardDto.getNumber())
            .code(createCardDto.getCode())
            .expirationDate(createCardDto.getExpirationDate())
            .build();
    creditCardRepository.save(card);
  }

  public void addCredits(
      final Long value, final Principal principal, final HttpServletRequest request) {
    final var user =
        userRepository
            .findByEmail(principal.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));

    user.setCurrency(user.getCurrency().add(BigDecimal.valueOf(value)));
    userRepository.save(user);
    authService.update(UserDetailsImpl.build(user), request);
  }

  public void deleteCreditCard(final Long id) {
    creditCardRepository.deleteById(id);
  }

  public void getImage(final Long id, final HttpServletResponse response) throws IOException {
    response.setContentType("image/png");
    final var user =
        userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

    var bytes = new ClassPathResource("/static/images/profile.png").getContentAsByteArray();

    if (Objects.nonNull(user.getProfilePicture())) {
      bytes = user.getProfilePicture();
    }

    final var is = new ByteArrayInputStream(bytes);
    IOUtils.copy(is, response.getOutputStream());
  }
}
