package com.semisvit.dto;

import com.semisvit.domain.AttributeType;

public record AttribDto(
        String attrName,
        String attribValue,
        AttributeType attributeType,
        String attribUnit) {
}
