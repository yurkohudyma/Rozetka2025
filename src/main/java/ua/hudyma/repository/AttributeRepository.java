package ua.hudyma.repository;

import ua.hudyma.domain.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttributeRepository extends JpaRepository<Attribute, Long> {
    boolean existsByAttributeName(String attributeName);

    List<Attribute> findAllByAttributeNameIn(List<String> toList);

    List<Attribute> findAllByCategoryList_CategoryName(String catName);

    Optional<Attribute> findByAttributeName(String attrName);
}
