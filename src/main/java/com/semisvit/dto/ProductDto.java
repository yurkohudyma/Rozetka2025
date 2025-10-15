package com.semisvit.dto;

import java.util.List;

public record ProductDto(
        String categoryName,
        String productName,
        String productCode,
        List<AttribDto> attributeList
        ) {
}

