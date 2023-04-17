package hr.algebra.toyswap.service;

import hr.algebra.toyswap.auth.UserDetailsImpl;
import hr.algebra.toyswap.dto.RegisterDto;
import hr.algebra.toyswap.model.user.User;
import hr.algebra.toyswap.model.user.UserRole;
import hr.algebra.toyswap.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public void register(final RegisterDto registerDto, HttpServletRequest request)
      throws IOException {
    final var user =
        User.builder()
            .email(registerDto.getEmail())
            .password(passwordEncoder.encode(registerDto.getPassword()))
            .firstName(registerDto.getFirstName())
            .lastName(registerDto.getLastName())
            .phoneNumber(registerDto.getPhoneNumber())
            .profilePicture(
                registerDto.getProfilePicture() != null
                    ? registerDto.getProfilePicture().getBytes()
                    : null)
            .role(UserRole.USER)
            .build();
    userRepository.save(user);

    final var userDetails = UserDetailsImpl.build(user);

    final var auth =
        new UsernamePasswordAuthenticationToken(
            userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    final var context = SecurityContextHolder.getContext();
    context.setAuthentication(auth);
    request
        .getSession()
        .setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
  }

  public void logout(final HttpServletRequest request) {
    request.getSession().invalidate();
  }

  //  public AuthResponseDto login(final LoginDto loginDto, final HttpServletRequest request) {
  //    if (!userRepository.existsByEmail(loginDto.getEmail())) {
  //      throw new AuthException("Email not in use");
  //    }
  //    final var password = passwordEncoder.encode(loginDto.getPassword());
  //    final var authentication =
  //        authenticationManager.authenticate(
  //            new UsernamePasswordAuthenticationToken(loginDto.getEmail(), password));
  //
  //    final var context = SecurityContextHolder.getContext();
  //    context.setAuthentication(authentication);
  //    request
  //        .getSession(true)
  //        .setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
  // context);
  //
  //    final var token = jwtUtils.generateToken(authentication);
  //    final var user =
  //        userRepository
  //            .findByEmail(loginDto.getEmail())
  //            .orElseThrow(() -> new RuntimeException("User not found"));
  //    return AuthResponseDto.builder()
  //        .token(token)
  //        .email(user.getEmail())
  //        .firstName(user.getFirstName())
  //        .lastName(user.getLastName())
  //        .role(user.getRole())
  //        .phoneNumber(user.getPhoneNumber())
  //        .profilePicture(user.getProfilePicture())
  //        .build();
  //  }
  //
  //  public AuthResponseDto register(final RegisterDto registerDto, final HttpServletRequest
  // request) {
  //    if (userRepository.existsByEmail(registerDto.getEmail())) {
  //      throw new AuthException("Email is already in use!");
  //    }
  //
  //    final var user =
  //        User.builder()
  //            .email(registerDto.getEmail())
  //            .password(passwordEncoder.encode(registerDto.getPassword()))
  //            .firstName(registerDto.getFirstName())
  //            .lastName(registerDto.getLastName())
  //            .role(UserRole.USER)
  //            .phoneNumber(registerDto.getPhoneNumber())
  //            .profilePicture(registerDto.getProfilePicture())
  //            .build();
  //
  //    userRepository.save(user);
  //    return login(
  //        LoginDto.builder()
  //            .email(registerDto.getEmail())
  //            .password(registerDto.getPassword())
  //            .build(),
  //        request);
  //  }
}
