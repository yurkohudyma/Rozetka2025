package ua.hudyma.repository;

import ua.hudyma.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByProductName(String productName);

    List<Product> findAllByCategory_CategoryName(String cat);

    List<Product> findAllByCategory_CategoryNameAndProductPriceBetween(String catName, BigDecimal min, BigDecimal max);


    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.productPropertiesList")
    List<Product> findAllWithProperties();

    Optional<Product> findByProductCode(String productCode);


    List<Product> findByProductPriceBetween(BigDecimal min, BigDecimal max);
}
