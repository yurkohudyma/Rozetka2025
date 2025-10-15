package ua.hudyma.dto;

import java.util.List;

public record ProductDto(
        String categoryName,
        String productName,
        String productCode,
        List<AttribDto> attributeList
        ) {
}

