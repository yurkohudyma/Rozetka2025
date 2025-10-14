package com.semisvit.mapper;

import com.semisvit.domain.Attribute;
import com.semisvit.dto.AttribDto;

public interface AttributeMapper {
    Attribute toDto (AttribDto dto);
    AttribDto toEntity (Attribute attribute);
}
