package com.semisvit.repository;

import com.semisvit.domain.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttributeRepository extends JpaRepository<Attribute, Long> {
    boolean existsByAttributeName(String attributeName);

    List<Attribute> findAllByAttributeNameIn(List<String> toList);
}
