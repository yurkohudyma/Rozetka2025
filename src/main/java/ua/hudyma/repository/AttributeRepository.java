package ua.hudyma.repository;

import ua.hudyma.domain.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.hudyma.dto.AttribDto;

import java.util.List;

public interface AttributeRepository extends JpaRepository<Attribute, Long> {
    boolean existsByAttributeName(String attributeName);

    List<Attribute> findAllByAttributeNameIn(List<String> toList);

    List<Attribute> findAllByCategory_CategoryName(String catName);
}
