package com.semisvit.repository;

import com.semisvit.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByProductName(String productName);
}
