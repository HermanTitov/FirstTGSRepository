package LaGavioTa.project.services;

import LaGavioTa.project.models.Ingredient;
import LaGavioTa.project.repositories.IngredientsRepository;
import LaGavioTa.project.util.errors.ObjectNotFoundException;
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
class IngredientsServiceTest {
    @Mock
    private IngredientsRepository ingredientsRepository;

    @InjectMocks
    private IngredientsService ingredientsService;

    // Arrange
    // Act
    // Assert

    @Test
    void findAll_ShouldReturnListOfIngredients() {
        // Arrange
        Ingredient ingredient1 = new Ingredient(1L, "Zanahoria");
        Ingredient ingredient2 = new Ingredient(2L, "Maiz");

        when(ingredientsRepository.findAll()).thenReturn(Arrays.asList(ingredient1, ingredient2));

        // Act
        List<Ingredient> ingredients = ingredientsService.findAll();

        // Assert

        assertNotNull(ingredients);
        assertEquals(2, ingredients.size());
        assertEquals("Zanahoria", ingredients.get(0).getName());
        assertEquals("Maiz", ingredients.get(1).getName());
        verify(ingredientsRepository, times(1)).findAll();
        verifyNoMoreInteractions(ingredientsRepository);
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenNoIngredients() {
        // Arrange
        when(ingredientsRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Ingredient> ingredients = ingredientsService.findAll();

        // Assert
        assertNotNull(ingredients);
        assertTrue(ingredients.isEmpty());
        verify(ingredientsRepository, times(1)).findAll();
        verifyNoMoreInteractions(ingredientsRepository);
    }

    @Test
    void findByName_ShouldReturnIngredient_WhenFound() {
        // Arrange
        Ingredient ingredient = new Ingredient(1L, "Zanahoria");

        // Act
        when(ingredientsRepository.findByName(ingredient.getName())).thenReturn(Optional.of(ingredient));

        Ingredient foundIngredient = ingredientsService.findByName(ingredient.getName());

        // Assert
        assertNotNull(foundIngredient);
        assertEquals(ingredient.getName(), foundIngredient.getName());
        verify(ingredientsRepository, times(1)).findByName(ingredient.getName());
        verifyNoMoreInteractions(ingredientsRepository);
    }

    @Test
    void findByName_ShouldThrowObjectNotFoundException_WhenNotFound() {
        // Arrange
        Ingredient ingredient = new Ingredient(1L, "Zanahoria");
        when(ingredientsRepository.findByName(ingredient.getName())).thenReturn(Optional.empty());

        // Act & Assert
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,
                () -> ingredientsService.findByName(ingredient.getName()));
        assertEquals("Ingredient with name Zanahoria not found.", exception.getMessage());
        verify(ingredientsRepository, times(1)).findByName(ingredient.getName());
        verifyNoMoreInteractions(ingredientsRepository);
    }

    @Test
    void findById_ShouldReturnIngredient_WhenFound() {
        // Arrange
        Ingredient ingredient = new Ingredient(1L, "Zanahoria");
        when(ingredientsRepository.findById(ingredient.getId())).thenReturn(Optional.of(ingredient));

        // Act
        Ingredient foundIngredient = ingredientsService.findById(ingredient.getId());

        // Assert
        assertNotNull(foundIngredient);
        assertEquals(ingredient, foundIngredient);
        verify(ingredientsRepository, times(1)).findById(ingredient.getId());
        verifyNoMoreInteractions(ingredientsRepository);

        /// Можно ли оставить так
        /// Мы сравниваем объект с объектом
        /// По идее, по хэш коду
        /// Либо по Id/Name сравнивать
        /// Либо че нахуй
    }

    @Test
    void findById_ShouldThrowObjectNotFoundException_WhenNotFound() {
        /// Arrange
        Ingredient ingredient = new Ingredient(99L, "Zanahoria");
        when(ingredientsRepository.findById(ingredient.getId())).thenReturn(Optional.empty());

        /// Act & Assert
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,
                () -> ingredientsService.findById(ingredient.getId()));
        assertEquals("Ingredient with id 99 not found.", exception.getMessage());
        verify(ingredientsRepository, times(1)).findById(ingredient.getId());
        verifyNoMoreInteractions(ingredientsRepository);
    }

}