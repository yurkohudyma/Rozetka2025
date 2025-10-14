package com.semisvit.dto;

import java.util.List;

public record ProductReqDto(
        String categoryName,
        String productName,
        List<AttribDto> attributeList
        ) {
}

