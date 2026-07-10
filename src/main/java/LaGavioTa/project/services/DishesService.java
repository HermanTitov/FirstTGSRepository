package LaGavioTa.project.services;

import LaGavioTa.project.models.Category;
import LaGavioTa.project.models.Dish;
import LaGavioTa.project.models.Ingredient;
import LaGavioTa.project.repositories.CategoriesRepository;
import LaGavioTa.project.repositories.DishesRepository;
import LaGavioTa.project.repositories.IngredientsRepository;
import LaGavioTa.project.util.errors.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class DishesService {
    private final DishesRepository dishesRepository;
    private final CategoriesService categoriesService;
    private final IngredientsService ingredientsService;

    private static final Set<String> SORT_CONSTANTS = Set.of("cost", "category");

    public List<Dish> findAll() {
        log.debug("Service: Fetching all dishes from database");
        List<Dish> dishes = dishesRepository.findAll();
        log.debug("Service: Successfully fetched {} dishes", dishes.size());
        return dishes;
    }

    /// Страничный поиск всех записей
    public Page<Dish> findAll(Pageable pageable) {
        log.debug("Service: Fetching paged dishes. Pageable context: {}", pageable);

        if (pageable.getSort().isSorted()){
            boolean isValidSort = pageable.getSort()
                    .stream().allMatch(order -> SORT_CONSTANTS.contains(order.getProperty()));
            if (!isValidSort) {
                log.warn("Service: Invalid sort field requested. Allowed fields: {}, Requested: {}",
                        SORT_CONSTANTS, pageable.getSort());
                throw new IllegalArgumentException("Not valid sort field" + SORT_CONSTANTS);
            }
        }
        return dishesRepository.findAll(pageable);
    }

    /// Публичный метод для контроллера
    public Dish getDish(Long id) {
        return findById(id);
    }

    private Dish findById(Long id) {
        log.debug("Service: Finding dish by id={}", id);
        return dishesRepository.findById(id).orElseThrow(() -> {
            log.warn("Service: Dish with id={} not found", id);
            return new ObjectNotFoundException(Dish.class.getSimpleName() + " with id " + id + " not found.");
        });
    }

    @Transactional
    public void create(Dish dish) {
        log.debug("Service: Attempting to create new dish with title='{}'", dish.getTitle());

        /// Поиск категории из БД
        Category category = categoriesService.findByName(dish.getCategory().getName());

        /// Поиск ингредиентов из БД
        List<Ingredient> ingredients = new ArrayList<>();
        for (Ingredient ingredient : dish.getIngredients()) {
            ingredients.add(ingredientsService.findByName(ingredient.getName()));
        }

        /// Создание сущности
        dish.setCategory(category);
        dish.setIngredients(ingredients);

        dishesRepository.save(dish);
        log.info("Service: Successfully saved new dish with id={}, title='{}', category='{}'",
                dish.getId(), dish.getTitle(), category.getName());
    }

    /// Обновление
    @Transactional
    public void update(Dish updatedDish, Long id) {
        log.debug("Service: Attempting to update dish id={}", id);

        /// Ищем блюдо в БД
        Dish dish = findById(id);

        /// Ищем категорию в БД
        Category category = categoriesService.findByName(updatedDish.getCategory().getName());

        /// Берем список имен ингредиентов из обновленного блюда
        List<String> ingredientNames = updatedDish.getIngredients().stream()
                .map(Ingredient::getName)
                .toList();

        /// Ищем ингредиенты в БД
        List<Ingredient> ingredients = ingredientsService.findAllByNames(ingredientNames);

        /// Обновляем объект
        dish.setCategory(category);
        dish.setIngredients(ingredients);
        dish.setCost(updatedDish.getCost());
        dish.setTitle(updatedDish.getTitle());
        dish.setDescription(updatedDish.getDescription());
        dish.setPrepTime(updatedDish.getPrepTime());
        dish.setPortionSize(updatedDish.getPortionSize());

        log.info("Service: Successfully updated dish id={}. Title='{}', Price={}",
                id, dish.getTitle(), dish.getCost());
    }

    /// Удаление
    @Transactional
    public void deleteById(Long id) {
        log.debug("Service: Attempting to delete dish id={}", id);

        if (!dishesRepository.existsById(id)) {
            log.warn("Service: Cannot delete dish. Id={} does not exist", id);
            throw new ObjectNotFoundException(Dish.class.getSimpleName() + " with id " + id + " not found.");
        }

        dishesRepository.deleteById(id);
        log.info("Service: Successfully deleted dish id={}", id);
    }
}
