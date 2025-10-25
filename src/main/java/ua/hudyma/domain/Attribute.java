package ua.hudyma.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "attributes")
@Data
public class Attribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NaturalId
    @Column(unique = true)
    private String attributeName;
    @Enumerated(EnumType.STRING)
    private AttributeType attributeType;  // STRING, NUMBER, BOOLEAN, SELECT

    /**
     * Було запропоновано для атрибутів типу SELECT, де користувач
     * повинен обрати одне значення з набору варіантів
     * (на кшталт Color: Red | Green | Blue).
     */
    /*@ElementCollection
    @CollectionTable(name = "attribute_options",
            joinColumns = @JoinColumn(name = "attribute_id"))
    @Column(name = "option_value")
    private List<String> options = new ArrayList<>();*/
    /*@ManyToOne
    private Category category;*/

    @ManyToMany
    @JoinTable(
            name = "category_attribute",
            joinColumns = @JoinColumn(name = "attribute_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categoryList = new ArrayList<>();
}

