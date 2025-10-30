package ua.hudyma.mapper;

import org.springframework.stereotype.Component;
import ua.hudyma.domain.orders.Order;
import ua.hudyma.domain.orders.OrderRespDto;

import java.util.List;

@Component
public class OrderMapperImpl implements OrderMapper{
    @Override
    public OrderRespDto toDto(Order order) {
        return new OrderRespDto(
                order.getId(),
                order.getOrderCode(),
                order.getOrderStatus(),
                order.getOrderTotal(),
                order.getBuyerCode(),
                order.getProductDTOList()
        );
    }

    @Override
    public Order toEntity(OrderRespDto dto) {
        var order = new Order();
        order.setOrderTotal(dto.orderTotal());
        order.setOrderCode(dto.orderCode());
        order.setId(dto.orderId());
        order.setOrderStatus(dto.orderStatus());
        order.getProductDTOList().addAll(dto.dtoList());
        return order;
    }

    @Override
    public List<OrderRespDto> toDtoList(List<Order> orderList) {
        return orderList.stream().map(this::toDto).toList();
    }

    @Override
    public List<Order> toEntityList(List<OrderRespDto> dtoList) {
        return dtoList.stream().map(this::toEntity).toList();
    }
}
