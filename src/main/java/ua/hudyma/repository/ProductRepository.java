package ua.hudyma.repository;

import ua.hudyma.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByProductName(String productName);

    List<Product> findAllByCategory_CategoryName(String cat);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.productPropertiesList")
    List<Product> findAllWithProperties();


}
