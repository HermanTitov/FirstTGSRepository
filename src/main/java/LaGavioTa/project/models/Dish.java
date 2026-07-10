package LaGavioTa.project.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.boot.convert.DurationFormat;
import org.springframework.boot.convert.DurationUnit;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Entity
@Table(name = "dishes")
@NoArgsConstructor
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    private Long id;

    @Column(name = "title")
    @Getter @Setter
    private String title;

    @Column(name="description")
    @Getter @Setter
    private String description;

    @Column (name="prep_time")
    @Getter @Setter
    @JdbcTypeCode(SqlTypes.INTERVAL_SECOND)
    //@DurationUnit(ChronoUnit.MINUTES)
    private Duration prepTime;

    @Column(name = "portion_size")
    @Getter @Setter
    private Integer portionSize;

    @Column(name = "cost")
    @Getter @Setter
    private BigDecimal cost;

    @Getter @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="dishes_ingredients",
            joinColumns = @JoinColumn(name = "dish_id", foreignKey = @ForeignKey(name="none", value = ConstraintMode.CONSTRAINT)),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    @Getter @Setter
    private List<Ingredient> ingredients;
}
