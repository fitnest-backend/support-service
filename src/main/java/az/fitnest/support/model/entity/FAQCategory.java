package az.fitnest.support.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "faq_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FAQCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;
}
