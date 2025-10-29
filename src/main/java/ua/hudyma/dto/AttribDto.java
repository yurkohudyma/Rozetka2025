package ua.hudyma.dto;

import ua.hudyma.enums.AttributeType;

public record AttribDto(
        String attrName,
        String attribValue,
        AttributeType attributeType,
        String attribUnit) {
}
