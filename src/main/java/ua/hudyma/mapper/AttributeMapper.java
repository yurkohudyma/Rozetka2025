package ua.hudyma.mapper;

import ua.hudyma.domain.products.Attribute;
import ua.hudyma.dto.AttribDto;
import ua.hudyma.dto.AttributeDetailsDto;

import java.util.List;

public interface AttributeMapper {
    AttribDto toDto (Attribute attribute);

    List<AttribDto> toDtoList(Attribute[] attributes);

    List<AttribDto> toDtoList(List<Attribute> attributes);
    List<AttributeDetailsDto> toAttribDetailsDtoList(List<Attribute> attributes);

    Attribute toEntity (AttribDto dto);
}
