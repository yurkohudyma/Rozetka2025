package ua.hudyma.mapper;

import ua.hudyma.domain.Attribute;
import ua.hudyma.dto.AttribDto;

public interface AttributeMapper {
    Attribute toDto (AttribDto dto);
    AttribDto toEntity (Attribute attribute);
}
