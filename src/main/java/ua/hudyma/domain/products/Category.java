package ua.hudyma.domain.products;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NaturalId
    @Column(unique = true)
    private String categoryName;
    @OneToMany(mappedBy = "category",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Product> productList = new ArrayList<>();
    @ManyToMany(mappedBy = "categoryList",
            cascade = CascadeType.ALL)
    private List<Attribute> attributesList = new ArrayList<>();
}

