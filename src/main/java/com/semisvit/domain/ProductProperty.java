package com.semisvit.domain;

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

/** Категорія: Цвяхи
        Атрибути:
            Довжина (число)
            Діаметр (число)
            Матеріал (select: Сталь, Алюміній...)
        Товар:
            Назва: "Цвяхи 50мм сталеві"
            Атрибути:
                Довжина = "50"
                Діаметр = "2"
                Матеріал = "Сталь"

 */
