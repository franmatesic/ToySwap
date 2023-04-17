package hr.algebra.toyswap.auth;

import hr.algebra.toyswap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) {
    final var user =
        userRepository
            .findByEmail(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
    return UserDetailsImpl.build(user);
  }
}
