package ua.hudyma.mapper;

import ua.hudyma.domain.orders.Order;
import ua.hudyma.domain.orders.OrderRespDto;

import java.util.List;

public interface OrderMapper {
    OrderRespDto toDto (Order order);
    Order toEntity (OrderRespDto dto);
    List<OrderRespDto> toDtoList (List<Order> orderList);
    List<Order> toEntityList (List<OrderRespDto>dtoList);
}
