package ua.hudyma.domain.products;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.NaturalId;
import ua.hudyma.util.IdGenerator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NaturalId
    @Column(unique = true)
    private String productCode;
    @Column(unique = true)
    private String productName;
    private String vendorCode;
    @ManyToOne
    private Category category;
    @OneToMany(mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true
            )
    private List<ProductProperty> productPropertiesList = new ArrayList<>();
    private BigDecimal productPrice;
    private String imageUrl;
    //todo multiple images url not persisted to DB, only first
}
