package ua.hudyma.repository;

import ua.hudyma.domain.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttributeRepository extends JpaRepository<Attribute, Long> {
    boolean existsByAttributeName(String attributeName);

    List<Attribute> findAllByAttributeNameIn(List<String> toList);
}
