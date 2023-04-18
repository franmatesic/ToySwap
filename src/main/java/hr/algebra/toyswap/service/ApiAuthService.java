package hr.algebra.toyswap.service;

import hr.algebra.toyswap.auth.JwtTokenUtils;
import hr.algebra.toyswap.auth.UserDetailsImpl;
import hr.algebra.toyswap.dto.AuthResponseDto;
import hr.algebra.toyswap.dto.LoginDto;
import hr.algebra.toyswap.dto.RegisterDto;
import hr.algebra.toyswap.exception.AuthException;
import hr.algebra.toyswap.model.user.User;
import hr.algebra.toyswap.model.user.UserRole;
import hr.algebra.toyswap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ApiAuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;

    public AuthResponseDto login(final LoginDto loginDto) {
        final var auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);

        final var userDetails = (UserDetailsImpl) auth.getPrincipal();
        final var token = jwtTokenUtils.generateToken(auth);

        return AuthResponseDto.builder()
                .token(token)
                .user(userDetails)
                .build();
    }

    public void register(final RegisterDto registerDto)
            throws IOException {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new AuthException("Email already in use");
        }
        final var user =
                User.builder()
                        .email(registerDto.getEmail())
                        .password(passwordEncoder.encode(registerDto.getPassword()))
                        .firstName(registerDto.getFirstName())
                        .lastName(registerDto.getLastName())
                        .phoneNumber(registerDto.getPhoneNumber())
                        .enabled(true)
                        .profilePicture(
                                registerDto.getProfilePicture() != null
                                        ? registerDto.getProfilePicture().getBytes()
                                        : null)
                        .role(UserRole.USER)
                        .build();
        userRepository.save(user);
    }
}
