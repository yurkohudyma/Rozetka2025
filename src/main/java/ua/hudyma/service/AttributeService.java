package ua.hudyma.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ua.hudyma.domain.AttributeUnit;
import ua.hudyma.domain.ProductProperty;
import ua.hudyma.dto.AttributeDetailsDto;
import ua.hudyma.repository.AttributeRepository;
import ua.hudyma.repository.AttributeUnitRepository;
import ua.hudyma.repository.ProductPropertyRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class AttributeService {

    private final AttributeRepository attributeRepository;
    private final AttributeUnitRepository attributeUnitRepository;
    private final ProductPropertyRepository productPropertyRepository;

    public List<String> getAttribValues(String name) {
        return productPropertyRepository
                .findAllValuesByAttribName(name)
                .stream()
                .map(ProductProperty::getValue)
                .toList();
    }

    //findAllValuesByAttribNameAndCatName

    public List<String> getAttribValuesByNameAndCatName(String attribName, String catName) {
        return productPropertyRepository
                .findAllValuesByAttribNameAndCatName(attribName, catName)
                .stream()
                .map(ProductProperty::getValue)
                .toList();
    }


    public AttributeDetailsDto getAttributeDetails(String name) {
        var attribute = attributeRepository.findByAttributeName(name)
                .orElseThrow(() -> new EntityNotFoundException("Attribute not found: " + name));

        var units = getAttribUnits(name);

        return new AttributeDetailsDto(
                attribute.getAttributeName(),
                attribute.getAttributeValue(),
                attribute.getAttributeType().name(),
                units
        );
    }

    public Set<String> getAttribUnits(String attribName) {
        return attributeUnitRepository.findAllByAttribute_AttributeName(attribName)
                .stream()
                .map(AttributeUnit::getAttribValue)
                .collect(Collectors.toSet());
    }
}

