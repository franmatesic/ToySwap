package hr.algebra.toyswap.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hr.algebra.toyswap.model.post.Condition;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CreatePostDto {

    @JsonIgnore
    private Long id;

    @NotNull(message = "Naslov je obavezan")
    @Length(min = 3, message = "Naslov mora biti duži od 3 znaka")
    private String title;

    @NotNull(message = "Opis je obavezan")
    @Length(min = 3, message = "Opis mora biti duži od 3 znaka")
    private String description;

    private Condition condition;

    @NotNull(message = "Cijena je obavezna")
    @PositiveOrZero(message = "Cijena mora biti pozitivan broj")
    private BigDecimal price;

    private List<TagDto> tags;

    @NotEmpty(message = "Barem jedna slika je obavezna")
    private List<MultipartFile> images;
}
