package ua.hudyma.mapper;

import ua.hudyma.domain.Attribute;
import ua.hudyma.dto.AttribDto;

public class AttributeMapperImpl implements AttributeMapper {

    @Override
    public AttribDto toEntity(Attribute attribute) {
        return new AttribDto(
                attribute.getAttributeName(),
                null,
                attribute.getAttributeType(),
                null
        );
    }

    @Override
    public Attribute toDto(AttribDto dto) {
        var attr = new Attribute();
        attr.setAttributeName(dto.attrName());
        attr.setAttributeType(dto.attributeType());
        return attr;
    }
}
