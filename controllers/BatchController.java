package LaGavioTa.project.controllers;

import LaGavioTa.project.dto.CategoryDTO;
import LaGavioTa.project.dto.DishFullDTO;
import LaGavioTa.project.dto.IngredientDTO;
import LaGavioTa.project.services.CategoriesService;
import LaGavioTa.project.services.DishesService;
import LaGavioTa.project.services.IngredientsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@RestController
//@RequestMapping("/batch")
//@RequiredArgsConstructor
//public class BatchController {
//
//    private final CategoryFacade categoryFacade;
//    private final IngredientFacade ingredientFacade;
//    private final DishFacade dishFacade;
//    private final CategoriesService categoriesService;
//    private final IngredientsService ingredientsService;
//    private final DishesService dishesService;
//
//    @PostMapping("/categories")
//    public ResponseEntity<Void> createCategory(@RequestBody List<CategoryDTO> categoriesDTO){
//        for (CategoryDTO category : categoriesDTO) {
//            categoriesService.create(categoryFacade.convertToEntity(category));
//        }
//        return new ResponseEntity<>(HttpStatus.CREATED);
//    }
//    @PostMapping("/ingredients")
//    public ResponseEntity<Void> createIngredient(@RequestBody List<IngredientDTO> ingredientsDTO){
//        for (IngredientDTO ingredient : ingredientsDTO) {
//            ingredientsService.create(ingredientFacade.convertToEntity(ingredient));
//        }
//        return new ResponseEntity<>(HttpStatus.CREATED);
//    }
//    @PostMapping("/dishes")
//    public ResponseEntity<Void> createDish(@RequestBody List<DishFullDTO> dishesFullDTO){
//        for (DishFullDTO dish : dishesFullDTO) {
//            dishesService.create(dishFacade.convertToEntity(dish));
//        }
//        return new ResponseEntity<>(HttpStatus.CREATED);
//    }
//}
