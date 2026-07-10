package LaGavioTa.project.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="ingredients")
@NoArgsConstructor
public class Ingredient {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    @Getter
    private Long id;

    @Column(name = "name")
    @Getter
    @Setter
    private String name;
}
