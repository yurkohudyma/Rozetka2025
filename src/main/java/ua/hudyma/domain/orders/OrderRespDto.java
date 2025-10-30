package ua.hudyma.domain.orders;

import java.math.BigDecimal;
import java.util.List;

public record OrderRespDto(
        Long orderId,
        String orderCode,
        OrderStatus orderStatus,
        BigDecimal orderTotal,
        String buyerCode,
        List<OrderProductDto> dtoList
) {
}
