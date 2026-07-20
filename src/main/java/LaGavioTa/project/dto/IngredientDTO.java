package LaGavioTa.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class IngredientDTO {
    @NotBlank
    @Size(min = 1, max = 100)
    public String name;
}
