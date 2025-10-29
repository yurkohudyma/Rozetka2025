package ua.hudyma.domain.orders;

import java.math.BigDecimal;
import java.util.List;

public record OrderRespDto(
        Long orderId,
        String orderCode,
        BigDecimal orderTotal,
        List<OrderProductDto> dtoList
) {
}
