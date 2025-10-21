package ua.hudyma.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record FilterReqDto(
        Map<String, List<String>> filterMap,
        BigDecimal minPrice,
        BigDecimal maxPrice) {}
