package hr.algebra.toyswap.auth;

import hr.algebra.toyswap.model.user.User;
import hr.algebra.toyswap.model.user.UserRole;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Builder
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

  private Long id;
  private String email;
  private String password;
  private String firstName;
  private String lastName;
  private UserRole role;
  private String phoneNumber;
  private byte[] profilePicture;

  public static UserDetailsImpl build(final User user) {
    return UserDetailsImpl.builder()
        .id(user.getId())
        .email(user.getEmail())
        .password(user.getPassword())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .role(user.getRole())
        .phoneNumber(user.getPhoneNumber())
        .profilePicture(user.getProfilePicture())
        .build();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
