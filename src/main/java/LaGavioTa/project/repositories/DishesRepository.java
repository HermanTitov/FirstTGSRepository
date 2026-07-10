package LaGavioTa.project.repositories;

import LaGavioTa.project.models.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface DishesRepository extends JpaRepository<Dish, Long> {
    /// N+1 Fetch запрос
    @Query("SELECT d FROM Dish d JOIN FETCH d.category JOIN FETCH d.ingredients")
    List<Dish> findAll();
    /// N+1 Fetch запрос
    @Query("SELECT d FROM Dish d JOIN FETCH d.category JOIN FETCH d.ingredients")
    Page<Dish> findAll(Pageable pageable);
}
