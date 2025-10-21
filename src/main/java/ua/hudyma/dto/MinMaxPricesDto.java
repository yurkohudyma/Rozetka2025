package ua.hudyma.dto;

import java.math.BigDecimal;

public record MinMaxPricesDto(
        BigDecimal maxPrice,
        BigDecimal minPrice
) {
}
