package ua.hudyma.domain;

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
    @OneToMany(mappedBy = "category")
    private List<Product> productList = new ArrayList<>();
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attribute> attributes = new ArrayList<>();
}

