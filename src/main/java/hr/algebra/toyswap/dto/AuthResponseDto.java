package hr.algebra.toyswap.dto;

import hr.algebra.toyswap.auth.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AuthResponseDto {

    private String token;
    private UserDetailsImpl user;
}
