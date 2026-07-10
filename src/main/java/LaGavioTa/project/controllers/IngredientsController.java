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
public class IngredientsController {

    private final IngredientMapper ingredientMapper;
    private final IngredientsService ingredientsService;

    @GetMapping
    public ResponseEntity<List<IngredientDTO>> getAllIngredients() {
     return ResponseEntity.ok(ingredientsService.findAll()
                .stream().map(ingredientMapper::convertToDTO).toList());
    }
    @GetMapping("/{id}")
    public ResponseEntity<IngredientDTO> getIngredientById(@PathVariable Long id) {
        return ResponseEntity.ok(ingredientMapper.convertToDTO(ingredientsService.findById(id)));
    }
    @PostMapping
    public ResponseEntity<Void> createIngredient(@Valid @RequestBody IngredientDTO ingredientDTO) {
        ingredientsService.create(ingredientMapper.convertToEntity(ingredientDTO));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateIngredientById(@PathVariable Long id, @Valid @RequestBody IngredientDTO ingredientDTO) {
        ingredientsService.update(ingredientMapper.convertToEntity(ingredientDTO), id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredientById(@PathVariable Long id) {
        ingredientsService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
