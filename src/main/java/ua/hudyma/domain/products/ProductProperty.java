package ua.hudyma.domain.products;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "product_properties")
@Data
public class ProductProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    private Product product;
    @ManyToOne
    private Attribute attribute;

    /**
     * Це універсальне поле, яке зберігає значення атрибута для товару,
     * незалежно від типу (STRING, NUMBER, BOOLEAN, SELECT).
     */
    private String value;
    private String attributeUnit;
}
