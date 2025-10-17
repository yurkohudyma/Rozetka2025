package ua.hudyma.mapper;

import org.springframework.stereotype.Component;
import ua.hudyma.domain.Attribute;
import ua.hudyma.dto.AttribDto;

@Component
public class AttributeMapperImpl implements AttributeMapper {

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
                null,
                attribute.getAttributeType(),
                null
        );
    }
}
