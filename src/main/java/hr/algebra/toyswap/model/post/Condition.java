package hr.algebra.toyswap.model.post;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Condition {
  NEW("Novo"),
  SHORT_USED("Kratko korišteno"),
  LONG_USED("Duže korišteno");

  private final String croatian;

  public static Condition byName(final String name) {
    return Arrays.stream(values())
        .filter(role -> role.name().equals(name))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Condition not found"));
  }
}
