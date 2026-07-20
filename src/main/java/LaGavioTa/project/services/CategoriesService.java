package LaGavioTa.project.services;

import LaGavioTa.project.models.Category;
import LaGavioTa.project.repositories.CategoriesRepository;
import LaGavioTa.project.util.errors.ObjectNotFoundException;
import LaGavioTa.project.util.errors.UniqueValueException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CategoriesService {
    private final CategoriesRepository categoriesRepository;

    public List<Category> findAll(){
        log.debug("Service: Fetching all categories from database");
        List<Category> categories = categoriesRepository.findAll();
        log.debug("Service: Successfully fetched {} categories", categories.size());
        return categories;
    }

    public Category findByName(String name) {
        log.debug("Service: Finding category by name='{}'", name);
        return categoriesRepository.findByName(name)
                .orElseThrow(() -> {
                    log.warn("Service: Category with name='{}' not found", name);
                    return new ObjectNotFoundException(Category.class.getSimpleName() + " with name " + name + " not found.");
                });
    }

    public Category findById(Long id) {
        log.debug("Service: Finding category by id={}", id);
        return categoriesRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Service: Category with id={} not found", id);
                    return new ObjectNotFoundException(Category.class.getSimpleName() + " with id " + id + " not found.");
                });
    }

    /// Создание категории
    @Transactional
    public void create(Category category) {
        log.debug("Service: Attempting to create new category with name='{}'", category.getName());

        if (categoriesRepository.findByName(category.getName()).isPresent()) {
            log.warn("Service: Cannot create category. Name '{}' already exists", category.getName());
            throw new UniqueValueException(Category.class.getSimpleName() + " with name " + category.getName() + " already exists.");
        }

        categoriesRepository.save(category);
        log.info("Service: Successfully saved new category with id={}, name='{}'", category.getId(), category.getName());
    }

    /// Обновление категории
    @Transactional
    public void update(Category updatedCategory, Long id) {
        log.debug("Service: Attempting to update category id={}", id);

        Category category = findById(id); // Внутри findById уже есть логирование debug/warn
        String oldName = category.getName();
        category.setName(updatedCategory.getName());

        log.info("Service: Successfully updated category id={}. Old name='{}', New name='{}'", id, oldName, updatedCategory.getName());
    }

    /// Удаление категории
    @Transactional
    public void deleteById(Long id) {
        log.debug("Service: Attempting to delete category id={}", id);

        if (!categoriesRepository.existsById(id)) {
            log.warn("Service: Cannot delete category. Id={} does not exist", id);
            throw new ObjectNotFoundException(Category.class.getSimpleName() + " with id " + id + " not found.");
        }

        categoriesRepository.deleteById(id);
        log.info("Service: Successfully deleted category id={}", id);
    }
}
