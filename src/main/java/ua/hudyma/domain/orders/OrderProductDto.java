package ua.hudyma.domain.orders;

public record OrderProductDto(
        String productCode,
        Integer quantity
) {
}
