package LaGavioTa.project.services;

import LaGavioTa.project.models.Category;
import LaGavioTa.project.repositories.CategoriesRepository;
import LaGavioTa.project.util.errors.ObjectNotFoundException;
import LaGavioTa.project.util.errors.UniqueValueException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoriesService {
    private final CategoriesRepository categoriesRepository;

    public List<Category> findAll(){ return categoriesRepository.findAll();}
    public Category findByName(String name) {
        return categoriesRepository.findByName(name).orElseThrow(() -> new ObjectNotFoundException(Category.class.getSimpleName() + " with name " + name + " not found."));
    }
    public Category findById(Long id ) {
        return categoriesRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(Category.class.getSimpleName() + " with id " + id + " not found."));
    }

    @Transactional
    public void create(Category category) {
        if (categoriesRepository.findByName(category.getName()).isPresent()) {
            throw new UniqueValueException(Category.class.getSimpleName() + " with name " + category.getName() + " already exists.");
        }
        categoriesRepository.save(category);
    }
    @Transactional
    public void deleteById(Long id) {
        if (categoriesRepository.existsById(id)) categoriesRepository.deleteById(id);
        else throw new ObjectNotFoundException(Category.class.getSimpleName() + " with id " + id + " not found.");}

    @Transactional
    public void update(Category updatedCategory, Long id) {
        Category category = findById(id);
        category.setName(updatedCategory.getName());
    }
}
