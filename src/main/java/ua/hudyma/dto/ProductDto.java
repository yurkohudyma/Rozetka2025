package ua.hudyma.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductDto(
        String categoryName,
        String productName,
        String productCode,
        BigDecimal productPrice,
        String vendorCode,
        List<AttribDto> attributeList
        ) {
}

