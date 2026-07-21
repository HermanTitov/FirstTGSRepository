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

    // Стабинг
    // when(что-то делается).thenReturn(верни)

    // Верификация
    // verify(репозиторий, количество вызовоз). метод

    // Утверждение
    // assertNotNull(result) - Объект не пустой
    // assertEquals(2,result.size) - размер списка 2
    // assertEquals("Электроника", result.get(0).getName()); -- Проверка, что первый объект - Электроника


    /// Тест структуры AAA Arange-Act-Assert
    @Test
    void findAll_ShouldReturnListOfCategories() {
        // Arrange
        Category cat1 = new Category(1L, "Электроника");
        Category cat2 = new Category(2L, "Книги");
        when(categoriesRepository.findAll()).thenReturn(Arrays.asList(cat1, cat2));

        // Act
        List<Category> result = categoriesService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Электроника", result.get(0).getName());
        assertEquals("Книги", result.get(1).getName());
        verify(categoriesRepository, times(1)).findAll();
        verifyNoMoreInteractions(categoriesRepository);
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenNoCategoriesExist() {
        // Arrange
        when(categoriesRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Category> result = categoriesService.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(categoriesRepository, times(1)).findAll();
    }

    @Test
    void findByName_ShouldReturnCategory_WhenFound() {
        // Arrange
        String name = "Книги"; // Имя категории
        Category category = new Category(1L, name); // Создали категорию
        // Сделай так и верни Optional<Category>
        when(categoriesRepository.findByName(name)).thenReturn(Optional.of(category));

        // Act
        Category result = categoriesService.findByName(name);

        // Assert
        assertNotNull(result); // Категория должна быть не null
        assertEquals(name, result.getName()); // Равная той, которую создали
        verify(categoriesRepository, times(1)).findByName(name); // Проверяем, что в репозиторий зашли 1 раз
        }

    @Test
    void findByName_ShouldThrowObjectNotFoundException_WhenNotFound() {
        // Arrange
        String name = "Несуществующая";
        /// Сделай так и потом верни Optional<Null/Empty>
        when(categoriesRepository.findByName(name)).thenReturn(Optional.empty());

        // Act & Assert
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,
                () -> categoriesService.findByName(name)
        );
        assertTrue(exception.getMessage().contains(name));
        verify(categoriesRepository, times(1)).findByName(name);
    }

    @Test
    void findById_ShouldReturnCategory_WhenFound() {
        // Arrange
        Long id = 1L;
        Category category = new Category(id, "Одежда");
        when(categoriesRepository.findById(id)).thenReturn(Optional.of(category));

        // Act
        Category result = categoriesService.findById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(categoriesRepository, times(1)).findById(id);
    }

    @Test
    void findById_ShouldThrowObjectNotFoundException_WhenNotFound() {
        // Arrange
        Long id = 99L;
        when(categoriesRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,
                () -> categoriesService.findById(id)
        );
        assertTrue(exception.getMessage().contains(id.toString()));
        verify(categoriesRepository, times(1)).findById(id);
    }

    @Test
    void create_ShouldSaveCategory_WhenNameIsUnique() {
        // Arrange
        Category newCategory = new Category(1L, "Спорт");
        when(categoriesRepository.findByName(newCategory.getName())).thenReturn(Optional.empty());

        // Act
        categoriesService.create(newCategory);

        // Assert
        verify(categoriesRepository, times(1)).findByName(newCategory.getName()); /// Проверка, что пошел в метод проверки
        verify(categoriesRepository, times(1)).save(newCategory); /// Проверка, что создавал категорию
    }

    @Test
    void create_ShouldThrowUniqueValueException_WhenNameAlreadyExists() {
        // Arrange
        Category newCategory = new Category(null, "Спорт");
        Category existingCategory = new Category(1L, "Спорт");
        when(categoriesRepository.findByName("Спорт")).thenReturn(Optional.of(existingCategory));

        // Act & Assert
        UniqueValueException exception = assertThrows(UniqueValueException.class,
                () -> categoriesService.create(newCategory)
        );
        assertTrue(exception.getMessage().contains(newCategory.getName()));
        verify(categoriesRepository, times(1)).findByName("Спорт");
        verify(categoriesRepository, never()).save(any(Category.class)); // Проверка, что класс никогда не вызывался

    }

    @Test
    void update_ShouldChangeName_WhenCategoryExists() {
        // Arrange
        Long id = 1L;
        Category databaseCategory = new Category(id, "Старое имя");
        Category updatedCategoryDetails = new Category(null, "Новое имя");

        when(categoriesRepository.findById(id)).thenReturn(Optional.of(databaseCategory));
        // Act
        categoriesService.update(updatedCategoryDetails, id);

        // Assert
        assertEquals("Новое имя", databaseCategory.getName());
        verify(categoriesRepository, times(1)).findById(id);
        // Метод save() в вашем сервисе не вызывается, так как JPA обновит данные автоматически по завершении транзакции
        }

    @Test
    void update_ShouldThrowObjectNotFoundException_WhenCategoryDoesNotExist() {
        // Arrange
        Long id = 99L;
        Category updatedCategoryDetails = new Category(null, "Новое имя");
        when(categoriesRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,
                () -> categoriesService.update(updatedCategoryDetails, id)
        );
        assertTrue(exception.getMessage().contains(id.toString()));
        verify(categoriesRepository, times(1)).findById(id);
    }

    @Test
    void deleteById_ShouldDelete_WhenCategoryExists() {
        // Arrange
        Long id = 1L;
        when(categoriesRepository.existsById(id)).thenReturn(true);

        // Act
        categoriesService.deleteById(id);

        // Assert
        verify(categoriesRepository, times(1)).existsById(id);
        verify(categoriesRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteById_ShouldThrowObjectNotFoundException_WhenIdDoesNotExist() {
        // Arrange
        Long id = 99L;
        when(categoriesRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,
                () -> categoriesService.deleteById(id)
        );
        assertTrue(exception.getMessage().contains(id.toString()));
        verify(categoriesRepository, times(1)).existsById(id);
        verify(categoriesRepository, never()).deleteById(anyLong());
    }
}
