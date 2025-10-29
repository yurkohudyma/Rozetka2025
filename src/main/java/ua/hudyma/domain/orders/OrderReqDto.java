package ua.hudyma.domain.orders;

import java.util.List;

public record OrderReqDto(
        List<OrderProductDto> productDTOList
) {
}
