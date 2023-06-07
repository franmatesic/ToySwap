package hr.algebra.toyswap.service;

import hr.algebra.toyswap.auth.UserDetailsImpl;
import hr.algebra.toyswap.dto.LoginDto;
import hr.algebra.toyswap.dto.RegisterDto;
import hr.algebra.toyswap.model.user.User;
import hr.algebra.toyswap.model.user.UserRole;
import hr.algebra.toyswap.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public String login(
      final LoginDto loginDto, final BindingResult result, final HttpServletRequest request) {
    final var user = userRepository.findByEmail(loginDto.getEmail());
    if (user.isEmpty()) {
      result.rejectValue("email", "login.email", "Korisnik ne postoji");
      return "login";
    }
    authenticate(UserDetailsImpl.build(user.get()), request);
    return "redirect:/home";
  }

  public String register(
      final RegisterDto registerDto, final BindingResult result, final HttpServletRequest request)
      throws IOException {
    if (userRepository.existsByEmail(registerDto.getEmail())) {
      result.rejectValue("email", "register.email", "Email se već koristi");
      return "register";
    }
    final var user =
        User.builder()
            .email(registerDto.getEmail())
            .password(passwordEncoder.encode(registerDto.getPassword()))
            .firstName(registerDto.getFirstName())
            .lastName(registerDto.getLastName())
            .phoneNumber(registerDto.getPhoneNumber())
            .currency(BigDecimal.ZERO)
            .enabled(true)
            .profilePicture(
                registerDto.getProfilePicture() != null
                    ? registerDto.getProfilePicture().getBytes()
                    : null)
            .role(UserRole.USER)
            .build();
    userRepository.save(user);

    authenticate(UserDetailsImpl.build(user), request);
    return "redirect:/home";
  }

  public void logout(final HttpServletRequest request) {
    SecurityContextHolder.clearContext();
    request.getSession().invalidate();
  }

  public void update(final UserDetailsImpl userDetails, final HttpServletRequest request) {
    authenticate(userDetails, request);
  }

  private void authenticate(final UserDetailsImpl userDetails, final HttpServletRequest request) {
    final var auth =
        new UsernamePasswordAuthenticationToken(
            userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    final var context = SecurityContextHolder.getContext();
    context.setAuthentication(auth);
    request
        .getSession()
        .setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
  }
}
