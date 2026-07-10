package LaGavioTa.project.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/// DTO только для вывода информации

public record DishShortDTO(@NotBlank @Size(min = 1, max = 100) String title,
                           @NotBlank @Size(min = 1, max = 255) String description,
                           @NotNull @Min(0) @Max(100_000) BigDecimal cost,
                           @Valid CategoryDTO category) {
}
