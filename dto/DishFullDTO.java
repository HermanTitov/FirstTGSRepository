package LaGavioTa.project.dto;


import LaGavioTa.project.models.Category;
import LaGavioTa.project.models.Ingredient;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Duration;
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
    @DecimalMin(valuet = 0.0, inclusive = true)
    private BigDecimal cost;

    @Valid
    private CategoryDTO category;

    @Valid
    @NotEmpty
    private List<@NotNull IngredientDTO> ingredients;
}
