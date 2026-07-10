package LaGavioTa.project.repositories;

import LaGavioTa.project.models.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IngredientsRepository extends JpaRepository<Ingredient, Long> {
    Optional <Ingredient> findByName(String name);

    List<Ingredient> findAllByNameIn(List<String> names);
}
