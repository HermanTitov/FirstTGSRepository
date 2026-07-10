package LaGavioTa.project.services;

import LaGavioTa.project.models.Ingredient;
import LaGavioTa.project.repositories.IngredientsRepository;
import LaGavioTa.project.util.errors.ObjectNotFoundException;
import LaGavioTa.project.util.errors.UniqueValueException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class IngredientsService {

    private final IngredientsRepository ingredientsRepository;

    public List<Ingredient> findAll() {
        return ingredientsRepository.findAll();
    }

    public Ingredient findById(long id) {
        return ingredientsRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(Ingredient.class.getSimpleName() + " with id " + id + " not found."));
    }

    public Ingredient findByName(String name) {
        return ingredientsRepository.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException(Ingredient.class.getSimpleName() + " with name " + name + " not found."));
    }

    @Transactional
    public void create(Ingredient ingredient) {
        if (ingredientsRepository.findByName(ingredient.getName()).isPresent()) {
            throw new UniqueValueException(Ingredient.class.getSimpleName() + " with name " + ingredient.getName() + " already exists.");
        }
        ingredientsRepository.save(ingredient);
    }

    @Transactional
    public void deleteById(long id) {
        if (!ingredientsRepository.existsById(id)) {
            throw new ObjectNotFoundException(Ingredient.class.getSimpleName() + " with id " + id + " not found.");
        }
        ingredientsRepository.deleteById(id);
    }

    public List<Ingredient> findAllByNames(List<String> ingredientNames) {
        List<Ingredient> found = ingredientsRepository.findAllByNameIn(ingredientNames);

        // Проверка: количество найденных в БД должно совпадать с запрошенными уникальными именами
        long uniqueRequestedCount = ingredientNames.stream().distinct().count();
        if (found.size() != uniqueRequestedCount) {
            throw new ObjectNotFoundException("Some ingredients were not found");
        }
        return found;
    }
    @Transactional
    public void update(Ingredient updatedIngredient, Long id) {
        Ingredient ingredient = findById(id);
        ingredient.setName(updatedIngredient.getName());
    }
}
