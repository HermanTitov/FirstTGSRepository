package LaGavioTa.project.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="categories")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    @Getter
    private Long id;

    @Column(name="name")
    @Getter
    @Setter
    private String name;
}
