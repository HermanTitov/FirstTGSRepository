package LaGavioTa.project.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DishFullDTO {

    @NotBlank
    @Size(min = 1, max = 100)
    private String title;

    @NotBlank
    @Size(min = 1, max = 100)
    private String description;

    @NotNull
    @Positive
    private Integer prepTimeMinutes;

    @NotNull
    @Positive
    private Integer portionSize;

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal cost;

    @Valid
    private CategoryDTO category;

    @Valid
    @NotEmpty
    private List<@NotNull IngredientDTO> ingredients;
}
