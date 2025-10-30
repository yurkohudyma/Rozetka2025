package ua.hudyma.domain.users;

public record BuyerRespDto(
        Long id,
        String buyerCode,
        String buyerName,
        String buyerAddress
) {
}
