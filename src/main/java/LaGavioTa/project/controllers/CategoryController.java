package LaGavioTa.project.controllers;

import LaGavioTa.project.dto.CategoryDTO;
import LaGavioTa.project.services.CategoriesService;
import LaGavioTa.project.util.mappers.CategoryMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryMapper categoryMapper;
    private final CategoriesService categoriesService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories(){
        return ResponseEntity.ok(categoriesService.findAll()
                .stream().map(categoryMapper::convertToDTO).toList());
    }
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id){
        return ResponseEntity.ok(categoryMapper.convertToDTO(categoriesService.findById(id)));
    }
    @PostMapping
    public ResponseEntity<Void> addCategory(@Valid @RequestBody CategoryDTO category){
        categoriesService.create(categoryMapper.convertToEntity(category));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO){
        categoriesService.update(categoryMapper.convertToEntity(categoryDTO), id);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id){
        categoriesService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
