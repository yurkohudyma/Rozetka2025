package ua.hudyma.domain.orders;

public record BuyerReqDto(
        String buyerFullName,
        String buyerAddress
) {
}
