package LaGavioTa.project.services;

import LaGavioTa.project.models.Ingredient;
import LaGavioTa.project.repositories.IngredientsRepository;
import LaGavioTa.project.util.errors.ObjectNotFoundException;
import LaGavioTa.project.util.errors.UniqueValueException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class IngredientsService {
    private final IngredientsRepository ingredientsRepository;

    public List<Ingredient> findAll() {
        log.debug("Service: Fetching all ingredients from database");
        List<Ingredient> ingredients = ingredientsRepository.findAll();
        log.debug("Service: Successfully fetched {} ingredients", ingredients.size());
        return ingredients;
    }

    public Ingredient findById(long id) {
        log.debug("Service: Finding ingredient by id={}", id);
        return ingredientsRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Service: Ingredient with id={} not found", id);
                    return new ObjectNotFoundException(Ingredient.class.getSimpleName() + " with id " + id + " not found.");
                });
    }

    public Ingredient findByName(String name) {
        log.debug("Service: Finding ingredient by name='{}'", name);
        return ingredientsRepository.findByName(name)
                .orElseThrow(() -> {
                    log.warn("Service: Ingredient with name='{}' not found", name);
                    return new ObjectNotFoundException(Ingredient.class.getSimpleName() + " with name " + name + " not found.");
                });
    }

    /// Создать ингредиент, если такого нет в БД
    @Transactional
    public void create(Ingredient ingredient) {
        log.debug("Service: Attempting to create new ingredient with name='{}'", ingredient.getName());

        if (ingredientsRepository.findByName(ingredient.getName()).isPresent()) {
            log.warn("Service: Cannot create ingredient. Name '{}' already exists", ingredient.getName());
            throw new UniqueValueException(Ingredient.class.getSimpleName() + " with name " + ingredient.getName() + " already exists.");
        }

        ingredientsRepository.save(ingredient);
        log.info("Service: Successfully saved new ingredient with id={}, name='{}'", ingredient.getId(), ingredient.getName());
    }

    /// Обновление ингредиента
    @Transactional
    public void update(Ingredient updatedIngredient, Long id) {
        log.debug("Service: Attempting to update ingredient id={}", id);

        Ingredient ingredient = findById(id); // Внутри findById уже есть логирование
        String oldName = ingredient.getName();
        ingredient.setName(updatedIngredient.getName());

        log.info("Service: Successfully updated ingredient id={}. Old name='{}', New name='{}'", id, oldName, updatedIngredient.getName());
    }

    /// Удалить ингредиент, если такой есть в БД
    @Transactional
    public void deleteById(long id) {
        log.debug("Service: Attempting to delete ingredient id={}", id);

        if (!ingredientsRepository.existsById(id)) {
            log.warn("Service: Cannot delete ingredient. Id={} does not exist", id);
            throw new ObjectNotFoundException(Ingredient.class.getSimpleName() + " with id " + id + " not found.");
        }

        ingredientsRepository.deleteById(id);
        log.info("Service: Successfully deleted ingredient id={}", id);
    }

    /// Найти список ингредиентов по имени в БД
    public List<Ingredient> findAllByNames(List<String> ingredientNames) {
        log.debug("Service: Finding ingredients by list of names: {}", ingredientNames);

        List<Ingredient> found = ingredientsRepository.findAllByNameIn(ingredientNames);
        long uniqueRequestedCount = ingredientNames.stream().distinct().count();

        if (found.size() != uniqueRequestedCount) {
            log.warn("Service: Bulk search failed. Requested unique names count: {}, but found only: {}",
                    uniqueRequestedCount, found.size());
            throw new ObjectNotFoundException("Some ingredients were not found");
        }

        log.debug("Service: Bulk search success. Found {} ingredients", found.size());
        return found;
    }
}
