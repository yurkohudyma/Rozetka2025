package com.semisvit.dto;

import java.util.List;

public record ProductRespDto(
        String productName,
        String catName,
        List<AttribDto> attribRespList

) {
}
