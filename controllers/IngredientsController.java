package LaGavioTa.project.controllers;

import LaGavioTa.project.dto.IngredientDTO;
import LaGavioTa.project.services.IngredientsService;
import LaGavioTa.project.util.mappers.IngredientMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ingredients")
@Slf4j // Аннотация от Lombok для активации логгера
public class IngredientsController {

    private final IngredientMapper ingredientMapper;
    private final IngredientsService ingredientsService;

    /// Получить ингредиенты
    @GetMapping
    public ResponseEntity<List<IngredientDTO>> getAllIngredients() {
        log.info("API Request: Get all ingredients");

        List<IngredientDTO> ingredients = ingredientsService.findAll()
                .stream().map(ingredientMapper::convertToDTO).toList();

        log.info("API Response: Returned {} ingredients", ingredients.size());
        return ResponseEntity.ok(ingredients);
    }

    /// Получить ингредиент
    @GetMapping("/{id}")
    public ResponseEntity<IngredientDTO> getIngredientById(@PathVariable Long id) {
        log.info("API Request: Get ingredient by id={}", id);

        IngredientDTO ingredientDTO = ingredientMapper.convertToDTO(ingredientsService.findById(id));

        log.info("API Response: Successfully retrieved ingredient id={}", id);
        return ResponseEntity.ok(ingredientDTO);
    }

    /// Добавить ингредиент
    @PostMapping
    public ResponseEntity<Void> createIngredient(@Valid @RequestBody IngredientDTO ingredientDTO) {
        log.info("API Request: Create new ingredient with name='{}'", ingredientDTO.getName());

        ingredientsService.create(ingredientMapper.convertToEntity(ingredientDTO));

        log.info("API Response: Ingredient successfully created");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /// Изменить ингредиент
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateIngredientById(@PathVariable Long id, @Valid @RequestBody IngredientDTO ingredientDTO) {
        log.info("API Request: Update ingredient id={}, new data='{}'", id, ingredientDTO);

        ingredientsService.update(ingredientMapper.convertToEntity(ingredientDTO), id);

        log.info("API Response: Ingredient id={} successfully updated", id);
        return ResponseEntity.ok().build();
    }

    /// Удалить ингредиент
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredientById(@PathVariable Long id) {
        log.info("API Request: Delete ingredient id={}", id);

        ingredientsService.deleteById(id);

        log.info("API Response: Ingredient id={} successfully deleted", id);
        return ResponseEntity.ok().build();
    }
}
