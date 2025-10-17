package ua.hudyma.mapper;

import ua.hudyma.domain.Attribute;
import ua.hudyma.dto.AttribDto;

public interface AttributeMapper {
    AttribDto toDto (Attribute attribute);
    Attribute toEntity (AttribDto dto);
}
