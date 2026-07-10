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
@Slf4j
public class CategoryController {
    private final CategoryMapper categoryMapper;
    private final CategoriesService categoriesService;

    /// Вывод всех категорий
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories(){
        log.info("API Request: Get all categories");

        List<CategoryDTO> categories = categoriesService.findAll()
                .stream().map(categoryMapper::convertToDTO).toList();

        log.info("API Response: Returned {} categories", categories.size());
        return ResponseEntity.ok(categories);
    }

    /// Вывод одной категории
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id){
        log.info("API Request: Get category by id={}", id);

        CategoryDTO categoryDTO = categoryMapper.convertToDTO(categoriesService.findById(id));

        log.info("API Response: Successfully retrieved category id={}", id);
        return ResponseEntity.ok(categoryDTO);
    }

    /// Добавление категории
    @PostMapping
    public ResponseEntity<Void> addCategory(@Valid @RequestBody CategoryDTO category){
        log.info("API Request: Create new category with name='{}'", category.getName());

        categoriesService.create(categoryMapper.convertToEntity(category));

        log.info("API Response: Category successfully created");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /// Обновление категории
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO){
        log.info("API Request: Update category id={}, new data='{}'", id, categoryDTO);

        categoriesService.update(categoryMapper.convertToEntity(categoryDTO), id);

        log.info("API Response: Category id={} successfully updated", id);
        return ResponseEntity.ok().build();
    }

    /// Удаление категории
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id){
        log.info("API Request: Delete category id={}", id);

        categoriesService.deleteById(id);

        log.info("API Response: Category id={} successfully deleted", id);
        return ResponseEntity.ok().build();
    }
}


