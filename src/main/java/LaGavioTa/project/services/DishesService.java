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
public class DishesService {
    private final DishesRepository dishesRepository;
    private final CategoriesService categoriesService;
    private final IngredientsService ingredientsService;
    private static final Set<String> SORT_CONSTANTS = Set.of("cost", "category");

    public List<Dish> findAll() {
        return dishesRepository.findAll();
    }

    public Page<Dish> findAll(Pageable pageable) {
        if (pageable.getSort().isSorted()){
            boolean isValidSort = pageable.getSort()
            .stream().allMatch(order -> SORT_CONSTANTS.contains(order.getProperty()));
            if (!isValidSort) {
                throw new IllegalArgumentException("Not valid sort field" + SORT_CONSTANTS);
            }
        }
        return dishesRepository.findAll(pageable);
    }
    private Dish findById(Long id) {
        return dishesRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(
                Dish.class.getSimpleName() + " with id " + id + " not found."
        ));
    }
    @Transactional
    public void create(Dish dish) {
        Category category = categoriesService.findByName(dish.getCategory().getName());
        List<Ingredient> ingredients = new ArrayList<>();
        for (Ingredient ingredient : dish.getIngredients()) {
            ingredients.add(ingredientsService.findByName(ingredient.getName()));
        }
        dish.setCategory(category);
        dish.setIngredients(ingredients);
        dishesRepository.save(dish);
    }
    @Transactional
    public void deleteById(Long id) {
        if (!dishesRepository.existsById(id)) {
            throw new ObjectNotFoundException(Dish.class.getSimpleName() + " with id " + id + " not found.");
        }
        dishesRepository.deleteById(id);
    }

    @Transactional
    public void update(Dish updatedDish, Long id) {
        // 1. Находим старое блюдо или сразу выбрасываем исключение (минус один лишний запрос)
        Dish dish = findById(id);
        // 2. Получаем новую категорию
        Category category = categoriesService.findByName(updatedDish.getCategory().getName());

        // 3. Собираем имена ВСЕХ новых ингредиентов в список
        List<String> ingredientNames = updatedDish.getIngredients().stream()
                .map(Ingredient::getName)
                .toList();

        // 4. Загружаем все ингредиенты ОДНИМ запросом из БД
        List<Ingredient> ingredients = ingredientsService.findAllByNames(ingredientNames);

        // 5. Обновляем поля старого объекта
        dish.setCategory(category);
        dish.setIngredients(ingredients);
        dish.setCost(updatedDish.getCost());
        dish.setTitle(updatedDish.getTitle());
        dish.setDescription(updatedDish.getDescription());
        dish.setPrepTime(updatedDish.getPrepTime());
        dish.setPortionSize(updatedDish.getPortionSize());
    }


}
