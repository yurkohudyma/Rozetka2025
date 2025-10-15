package com.semisvit.repository;

import com.semisvit.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByProductName(String productName);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.productPropertiesList")
    List<Product> findAllWithProperties();


}
