package LaGavioTa.project.controllers;

import LaGavioTa.project.dto.DishFullDTO;
import LaGavioTa.project.dto.DishShortDTO;
import LaGavioTa.project.services.DishesService;
import LaGavioTa.project.util.mappers.DishMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
@Slf4j
public class MenuController {

    private final DishesService dishesService;
    private final DishMapper dishMapper;

    @GetMapping("/all")
    public ResponseEntity<List<DishFullDTO>> getAllDishes() {
        log.debug("Get all dishes");
        List<DishFullDTO> dishes = dishesService.findAll()
                .stream().map(dishMapper::convertToFullDTO).toList();
        return ResponseEntity.ok(dishes);
    }
@GetMapping
    public ResponseEntity<List<DishShortDTO>> getAllDishesShort() {
        log.debug("Get all dishes short");
        List<DishShortDTO> dishesShort = dishesService.findAll()
                .stream().map(dishMapper::convertToShortDTO).toList();
        return ResponseEntity.ok(dishesShort);
    }
    @GetMapping("/page")
    public ResponseEntity<PagedModel<DishShortDTO>> getAllDishesShortByPage(@PageableDefault(sort = "category") Pageable pageable) {
        log.debug("Get dishes short by page: {}", pageable);
        PagedModel<DishShortDTO> pages = new PagedModel<> (dishesService.findAll(pageable).map(dishMapper::convertToShortDTO));
        return ResponseEntity.ok(pages);
    }

    @PostMapping
    public ResponseEntity<Void> createDish(@Valid @RequestBody DishFullDTO dishFullDTO) {
        log.debug("Creating new dish");
        dishesService.create(dishMapper.convertToEntity(dishFullDTO));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateDish(@PathVariable Long id, @Valid @RequestBody DishFullDTO dishFullDTO) {
        dishesService.update(dishMapper.convertToEntity(dishFullDTO),id);
        return  ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable Long id) {
        log.debug("Deleting dish with id: {}", id);
        dishesService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
/// PagedModel - обертка над Page - нужна в случае изменений в структуре Page
