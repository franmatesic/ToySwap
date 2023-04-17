package hr.algebra.toyswap.model.user;

import java.util.Arrays;

public enum UserRole {
  USER,
  ADMIN;

  public static UserRole byName(final String name) {
    return Arrays.stream(values())
        .filter(role -> role.name().equals(name))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Role not found"));
  }
}
