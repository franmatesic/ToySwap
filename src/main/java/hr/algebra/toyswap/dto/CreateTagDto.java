package hr.algebra.toyswap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateTagDto {

    @NotNull
    @NotBlank
    private String name;
}
