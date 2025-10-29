package ua.hudyma.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.hudyma.domain.products.Attribute;
import ua.hudyma.dto.AttribDto;
import ua.hudyma.dto.AttributeDetailsDto;
import ua.hudyma.service.AttributeService;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AttributeMapperImpl implements AttributeMapper {

    private final AttributeService attributeService;

    @Override
    public Attribute toEntity(AttribDto dto) {
        var attribute = new Attribute();
        attribute.setAttributeName(dto.attrName());
        attribute.setAttributeType(dto.attributeType());
        return attribute;
    }

    @Override
    public AttribDto toDto(Attribute attribute) {
        return new AttribDto(
                attribute.getAttributeName(),
                attribute.getAttributeValue(),
                attribute.getAttributeType(),
                null
        );
    }

    @Override
    public List<AttribDto> toDtoList(Attribute[] attributes) {
        return Arrays
                .stream(attributes)
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<AttribDto> toDtoList(List<Attribute> attributes) {
        return attributes
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<AttributeDetailsDto> toAttribDetailsDtoList(List<Attribute> attributes) {
        return attributes
                .stream()
                .map(attribute -> new AttributeDetailsDto(
                        attribute.getAttributeName(),
                        attribute.getAttributeValue(),
                        attribute.getAttributeType().name(),
                        attributeService.getAttribUnits(attribute.getAttributeName())
                ))
                .toList();
    }
}
