package LaGavioTa.project.services;

import LaGavioTa.project.models.Ingredient;
import LaGavioTa.project.repositories.IngredientsRepository;
import LaGavioTa.project.util.errors.ObjectNotFoundException;
import LaGavioTa.project.util.errors.UniqueValueException;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@ExtendWith(MockitoExtension.class) // Подключает Mockito к JUnit5
class IngredientsServiceTest {
    @Mock
    private IngredientsRepository ingredientsRepository;

    @InjectMocks
    private IngredientsService ingredientsService;

    @Test
    void findAllByNames_ShouldReturnListOfIngredients(){
        Long id1 = 1L;
        Long id2 = 2L;
        String name1 = "Морковь";
        String name2 = "Кукуруза";
        List<Ingredient> searchingIngredients = List.of(new Ingredient(id1,name1), new Ingredient(id2,name2));
        when(ingredientsRepository.findAllByNameIn(List.of(name1,name2))).thenReturn(searchingIngredients);

        List<Ingredient> returnedIngredients = ingredientsService.findAllByNames(List.of(name1,name2));

        assertEquals(id1,returnedIngredients.get(0).getId());
        assertEquals(id2,returnedIngredients.get(1).getId());
        assertEquals(2, returnedIngredients.size());
    }
    @Test
    void findAllByNames_ShouldThrowsObjectNotFoundException_WhenNoFoundOne(){
        when(ingredientsRepository.findAllByNameIn(List.of("Морковь","Кукуруза"))).thenReturn(List.of(new Ingredient(1L,"Кукуруза")));

        ObjectNotFoundException exception = assertThrows(
                ObjectNotFoundException.class,
                () -> ingredientsService.findAllByNames(List.of("Морковь","Кукуруза")));
        assertTrue(exception.getMessage().contains("Some ingredients were not found"));
    }

    @Test
    void findAll_ShouldReturnListOfIngredients() {
        Ingredient ingredient1 = new Ingredient(1L, "Морковь");
        Ingredient ingredient2 = new Ingredient(2L, "Кукуруза");
        when(ingredientsRepository.findAll()).thenReturn(Arrays.asList(ingredient1, ingredient2));

        List<Ingredient> ingredients = ingredientsService.findAll();

        assertEquals(2, ingredients.size());
        assertEquals("Морковь", ingredients.get(0).getName());
        assertEquals("Кукуруза", ingredients.get(1).getName());
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenNoIngredients() {
        when(ingredientsRepository.findAll()).thenReturn(Collections.emptyList());

        List<Ingredient> ingredients = ingredientsService.findAll();

        assertTrue(ingredients.isEmpty());
    }

    @Test
    void findByName_ShouldReturnIngredient_WhenFound() {
        Long id = 1L;
        String name = "Морковь";
        when(ingredientsRepository.findByName(name)).thenReturn(Optional.of(new Ingredient(id,name)));

        Ingredient foundIngredient = ingredientsService.findByName(name);

        assertEquals(name, foundIngredient.getName());
    }

    @Test
    void findByName_ShouldThrowObjectNotFoundException_WhenNotFound() {
        String name = "Несуществующий";
        when(ingredientsRepository.findByName(name)).thenReturn(Optional.empty());

        ObjectNotFoundException exception = assertThrows(
                ObjectNotFoundException.class,()
                -> ingredientsService.findByName(name));

        assertTrue(exception.getMessage().contains(name));
    }

    @Test
    void findById_ShouldReturnIngredient_WhenFound() {
        Long id = 1L;
        when(ingredientsRepository.findById(id)).thenReturn(Optional.of(new Ingredient(id,null)));

        Ingredient foundIngredient = ingredientsService.findById(id);

        assertEquals(id, foundIngredient.getId());
    }

    @Test
    void findById_ShouldThrowObjectNotFoundException_WhenNotFound() {
        Long id = 99L;
        when(ingredientsRepository.findById(id)).thenReturn(Optional.empty());

        ObjectNotFoundException exception = assertThrows(
                ObjectNotFoundException.class,
                () -> ingredientsService.findById(id));

        assertTrue(exception.getMessage().contains(id.toString()));
    }
    @Test
    void deleteById_ShouldDeleteIngredient_WhenIngredientExists() {
        Long id = 1L;
        when(ingredientsRepository.existsById(id)).thenReturn(true);

        ingredientsService.deleteById(id);

        verify(ingredientsRepository).existsById(id);
        verify(ingredientsRepository).deleteById(id);
    }

    @Test
    void deleteById_ShouldThrowObjectNotFoundException_WhenIdDoesNotExist() {
        Long id = 99L;
        when(ingredientsRepository.existsById(id)).thenReturn(false);

        ObjectNotFoundException exception = assertThrows(
                ObjectNotFoundException.class,
                () -> ingredientsService.deleteById(id));

        assertTrue(exception.getMessage().contains(id.toString()));
        verify(ingredientsRepository).existsById(id);
        verifyNoMoreInteractions(ingredientsRepository);
    }

    @Test
    void create_ShouldCreateIngredient_WhenNameIsUnique() {
        Long id = 1L;
        String name = "Морковь";
        Ingredient ingredientToSave= new Ingredient(null, name);
        when(ingredientsRepository.findByName(name)).thenReturn(Optional.empty());
        when(ingredientsRepository.save(any(Ingredient.class))).thenReturn(new Ingredient(id, name));

        ingredientsService.create(ingredientToSave);

        verify(ingredientsRepository).findByName(name);
        verify(ingredientsRepository).save(ingredientToSave);
    }

    @Test
    void create_ShouldThrowUniqueValueException_WhenNameAlreadyExists() {
        Long id = 1L;
        String name = "Морковь";
        Ingredient ingredientToSave = new Ingredient(null,name);
        when(ingredientsRepository.findByName(name)).thenReturn(Optional.of(new Ingredient(id, name)));

        UniqueValueException exception = assertThrows(
                UniqueValueException.class,
                () -> ingredientsService.create(ingredientToSave));

        assertTrue(exception.getMessage().contains(name));
        verify(ingredientsRepository).findByName(name);
        verify(ingredientsRepository, never()).save(any(Ingredient.class));
    }

    @Test
    void update_ShouldUpdateIngredient_WhenIngredientFound() {
        Long id = 1L;
        String oldName = "Морковь";
        String newName = "Кукуруза";
        Ingredient oldIngredient = new Ingredient(id, oldName);
        Ingredient newIngredient = new Ingredient(null, newName);
        when(ingredientsRepository.findById(id)).thenReturn(Optional.of(oldIngredient));

        ingredientsService.update(newIngredient, id);

        assertEquals(newName, oldIngredient.getName());
        verify(ingredientsRepository).findById(id);
    }
    @Test
    void update_ShouldThrowObjectNotFoundException_WhenIdDoesNotFound() {
        Long id = 99L;
        Ingredient updatedIngredient = new Ingredient(1L, "Морковь");
        when(ingredientsRepository.findById(id)).thenReturn(Optional.empty());

        ObjectNotFoundException exception = assertThrows(
                ObjectNotFoundException.class,
                ()-> ingredientsService.update(updatedIngredient, id));
        assertTrue(exception.getMessage().contains(id.toString()));
        verify(ingredientsRepository).findById(id);
        verifyNoMoreInteractions(ingredientsRepository);
    }
}

    // Arrange
    // Act
    // Assert
    // verify(ingredientsRepository, times(1)).findByName(ingredient.getName());
    // verifyNoMoreInteractions(ingredientsRepository);
    // verify(ingredientsRepository, never()).deleteById(id);


    /// Оставлю на будущее
    /// ПЕРЕОПРЕДЕЛИТЬ МЕТОДЫ hashCode() и equals() и СРАВНИВАТЬ В ТЕСТАХ ОБЪЕКТЫ
    // Метод упадет с шикарным логом, если ХОТЯ БЫ ОДНО поле внутри объекта не совпадет
    // assertThat(foundIngredient).isEqualTo(expectedIngredient);


    // Стабинг
    // when(что-то делается).thenReturn(верни)

    // Верификация
    // verify(репозиторий, количество вызовоз). метод

    // Утверждение
    // assertNotNull(result) - Объект не пустой
    // assertEquals(2,result.size) - размер списка 2
    // assertEquals("Салаты", result.get(0).getName()); -- Проверка, что первый объект - Салаты


    /// Тест структуры AAA Arange-Act-Assert
