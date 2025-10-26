package ua.hudyma.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ua.hudyma.domain.AttributeUnit;
import ua.hudyma.dto.AttributeDetailsDto;
import ua.hudyma.repository.AttributeRepository;
import ua.hudyma.repository.AttributeUnitRepository;

@Service
@RequiredArgsConstructor
@Log4j2
public class AttributeService {

    private final AttributeRepository attributeRepository;
    private final AttributeUnitRepository attributeUnitRepository;

    public AttributeDetailsDto getAttributeDetails(String name) {
        var attribute = attributeRepository.findByAttributeName(name)
                .orElseThrow(() -> new EntityNotFoundException("Attribute not found: " + name));

        var units = attributeUnitRepository.findAllByAttribute_AttributeName(name)
                .stream()
                .map(AttributeUnit::getValue)
                .toList();

        return new AttributeDetailsDto(
                attribute.getAttributeName(),
                attribute.getAttributeValue(),
                attribute.getAttributeType().name(),
                units
        );
    }
}

