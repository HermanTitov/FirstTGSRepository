package LaGavioTa.project.services;

import LaGavioTa.project.models.Category;
import LaGavioTa.project.repositories.CategoriesRepository;
import LaGavioTa.project.util.errors.ObjectNotFoundException;
import LaGavioTa.project.util.errors.UniqueValueException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Подключает Mockito к JUnit5
class CategoriesServiceTest {

    @Mock
    private CategoriesRepository categoriesRepository; /// Создает фальшивый репозиторий к БД

    @InjectMocks
    private CategoriesService categoriesService; /// Создает реальный объект Категории - сервис

    @Test
    void findAll_ShouldReturnListOfCategories() {
        Category cat1 = new Category(1L, "Салаты");
        Category cat2 = new Category(2L, "Супы");
        when(categoriesRepository.findAll()).thenReturn(Arrays.asList(cat1, cat2));

        List<Category> categories = categoriesService.findAll();

        assertEquals(2, categories.size());
        assertEquals("Салаты", categories.get(0).getName());
        assertEquals("Супы", categories.get(1).getName());
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenNoCategoriesExist() {
        when(categoriesRepository.findAll()).thenReturn(Collections.emptyList());

        List<Category> categories = categoriesService.findAll();

        // Assert
        assertTrue(categories.isEmpty());
    }
    @Test
    void findByName_ShouldReturnCategory_WhenFound() {
        Long id = 1L;
        String name = "Супы"; // Имя категории
        when(categoriesRepository.findByName(name)).thenReturn(Optional.of(new Category(id, name)));

        Category category = categoriesService.findByName(name);

        assertEquals(name, category.getName());
    }

    @Test
    void findByName_ShouldThrowObjectNotFoundException_WhenNotFound() {
        String name = "Несуществующая";
        when(categoriesRepository.findByName(name)).thenReturn(Optional.empty());

        ObjectNotFoundException exception = assertThrows(
                ObjectNotFoundException.class,
                () -> categoriesService.findByName(name));
        assertTrue(exception.getMessage().contains(name));
    }

    @Test
    void findById_ShouldReturnCategory_WhenFound() {
        Long id = 1L;
        Category category = new Category(id, "Салаты");
        when(categoriesRepository.findById(id)).thenReturn(Optional.of(category));

        // Act
        Category foundCategory = categoriesService.findById(id);

        assertEquals(id, foundCategory.getId());
    }

    @Test
    void findById_ShouldThrowObjectNotFoundException_WhenNotFound() {
        Long id = 99L;
        when(categoriesRepository.findById(id)).thenReturn(Optional.empty());

        ObjectNotFoundException exception = assertThrows(
                ObjectNotFoundException.class,
                () -> categoriesService.findById(id));
        assertTrue(exception.getMessage().contains(id.toString()));
    }
    @Test
    void deleteById_ShouldDelete_WhenCategoryExists() {
        Long id = 1L;
        when(categoriesRepository.existsById(id)).thenReturn(true);

        categoriesService.deleteById(id);

        verify(categoriesRepository).existsById(id);
        verify(categoriesRepository).deleteById(id);
    }

    @Test
    void deleteById_ShouldThrowObjectNotFoundException_WhenIdDoesNotExist() {
        Long id = 99L;
        when(categoriesRepository.existsById(id)).thenReturn(false);

        ObjectNotFoundException exception = assertThrows(
                ObjectNotFoundException.class,
                () -> categoriesService.deleteById(id));
        assertTrue(exception.getMessage().contains(id.toString()));
        verify(categoriesRepository, times(1)).existsById(id);
        verifyNoMoreInteractions(categoriesRepository);
    }
     @Test
    void create_ShouldSaveCategory_WhenNameIsUnique() {
        Long id = 1L;
        String name = "Салаты";
        Category categoryToSave = new Category(null, name);
        when(categoriesRepository.findByName(name)).thenReturn(Optional.empty());
        when(categoriesRepository.save(any(Category.class))).thenReturn(new Category(id, name));

        categoriesService.create(categoryToSave);

        verify(categoriesRepository).findByName(name);
        verify(categoriesRepository).save(categoryToSave);
    }

    @Test
    void create_ShouldThrowUniqueValueException_WhenNameAlreadyExists() {
        Long id = 1L;
        String name = "Салаты";
        Category categoryToSave = new Category(null, name);
        when(categoriesRepository.findByName(name)).thenReturn(Optional.of(new Category(id, name)));

        UniqueValueException exception = assertThrows(
                UniqueValueException.class,
                () -> categoriesService.create(categoryToSave));

        assertTrue(exception.getMessage().contains(name));
        verify(categoriesRepository).findByName(name);
        verify(categoriesRepository, never()).save(any(Category.class));
    }

    @Test
    void update_ShouldChangeName_WhenCategoryExists() {
        Long id = 1L;
        String oldName = "Салаты";
        String newName = "Супы";
        Category oldCategory = new Category(id, oldName);
        Category updatedCategory = new Category(null, newName);
        when(categoriesRepository.findById(id)).thenReturn(Optional.of(oldCategory));

        categoriesService.update(updatedCategory, id);

        assertEquals(newName, oldCategory.getName());
        verify(categoriesRepository).findById(id);
        }

    @Test
    void update_ShouldThrowObjectNotFoundException_WhenCategoryDoesNotExist() {
        Long id = 99L;
        String name = "Салаты";
        Category updatedCategory = new Category(null, name);
        when(categoriesRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        ObjectNotFoundException exception = assertThrows(
                ObjectNotFoundException.class,
                () -> categoriesService.update(updatedCategory, id));

        assertTrue(exception.getMessage().contains(id.toString()));
        verifyNoMoreInteractions(categoriesRepository);
    }
}
