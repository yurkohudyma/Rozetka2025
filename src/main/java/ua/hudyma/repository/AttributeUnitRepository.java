package ua.hudyma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.hudyma.domain.AttributeUnit;

import java.util.List;

public interface AttributeUnitRepository extends JpaRepository<AttributeUnit, Long> {

    List<AttributeUnit> findAllByAttribute_AttributeName (String attrName);

    boolean existsByAttribute_IdAndAttribValue(Long id, String value);
}
