package LaGavioTa.project.controllers;

import LaGavioTa.project.dto.DishFullDTO;
import LaGavioTa.project.dto.DishShortDTO;
import LaGavioTa.project.services.DishesService;
import LaGavioTa.project.util.mappers.DishMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/dishes") // Предполагаемый маппинг для контекста блюд
@Slf4j
public class MenuController {

    private final DishesService dishesService;
    private final DishMapper dishMapper;

    /// Вывод всех блюд
    @GetMapping("/all")
    public ResponseEntity<List<DishFullDTO>> getAllDishes() {
        log.info("API Request: Get all full dishes");

        List<DishFullDTO> dishes = dishesService.findAll()
                .stream().map(dishMapper::convertToFullDTO).toList();

        log.info("API Response: Returned {} full dishes", dishes.size());
        return ResponseEntity.ok(dishes);
    }

    /// Получить все блюда короткими
    @GetMapping
    public ResponseEntity<List<DishShortDTO>> getAllDishesShort() {
        log.info("API Request: Get all short dishes");

        List<DishShortDTO> dishesShort = dishesService.findAll()
                .stream().map(dishMapper::convertToShortDTO).toList();

        log.info("API Response: Returned {} short dishes", dishesShort.size());
        return ResponseEntity.ok(dishesShort);
    }

    /// Получить полное блюдо
    @GetMapping("/{id}")
    public ResponseEntity<DishFullDTO> getDishFullDTO(@PathVariable Long id){
        log.info("API Request: Get full dish by id={}", id);

        DishFullDTO dishFullDTO = dishMapper.convertToFullDTO(dishesService.getDish(id));

        log.info("API Response: Successfully retrieved dish id={}", id);
        return ResponseEntity.ok(dishFullDTO); // Исправлен пропущенный объект в ответе
    }

    /// Получить страничку блюд
    @GetMapping("/page")
    public ResponseEntity<PagedModel<DishShortDTO>> getAllDishesShortByPage(@PageableDefault(sort = "category") Pageable pageable) {
        log.info("API Request: Get dishes page. Page number={}, Page size={}, Sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        PagedModel<DishShortDTO> pages = new PagedModel<>(dishesService.findAll(pageable).map(dishMapper::convertToShortDTO));

        log.info("API Response: Returned page with {} dishes. Total elements={}",
                pages.getContent().size(), pages.getMetadata().totalElements());
        return ResponseEntity.ok(pages);
    }

    /// Добавление блюда
    @PostMapping
    public ResponseEntity<Void> createDish(@Valid @RequestBody DishFullDTO dishFullDTO) {
        log.info("API Request: Create new dish with name='{}'", dishFullDTO.getTitle());

        dishesService.create(dishMapper.convertToEntity(dishFullDTO));

        log.info("API Response: Dish successfully created");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /// Обновление блюда
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateDish(@PathVariable Long id, @Valid @RequestBody DishFullDTO dishFullDTO) {
        log.info("API Request: Update dish id={}, new data='{}'", id, dishFullDTO);

        dishesService.update(dishMapper.convertToEntity(dishFullDTO), id);

        log.info("API Response: Dish id={} successfully updated", id);
        return ResponseEntity.ok().build();
    }

    /// Удаление блюда
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable Long id) {
        log.info("API Request: Delete dish id={}", id);

        dishesService.deleteById(id);

        log.info("API Response: Dish id={} successfully deleted", id);
        return ResponseEntity.ok().build();
    }
}
/// PagedModel - обертка над Page - нужна в случае изменений в структуре Page
