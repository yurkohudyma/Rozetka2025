package com.semisvit.repository;

import com.semisvit.domain.ProductProperty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Field;
import java.util.Optional;

public interface ProductPropertyRepository extends JpaRepository<ProductProperty, Long> {
    boolean existsByAttributeId(Long id);

    Optional<ProductProperty> findByAttributeId(Long id);
}
