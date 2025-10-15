package ua.hudyma.repository;

import ua.hudyma.domain.ProductProperty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductPropertyRepository extends JpaRepository<ProductProperty, Long> {
    boolean existsByAttributeId(Long id);

    Optional<ProductProperty> findByAttributeId(Long id);
}
