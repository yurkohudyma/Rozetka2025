package ua.hudyma.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ua.hudyma.domain.orders.*;
import ua.hudyma.repository.OrderRepository;
import ua.hudyma.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderRespDto createOrder(List<OrderProductDto> dtoList) {
        var order = new Order();
        order.setOrderStatus(OrderStatus.CONFIRMED);
        order.setOrderTotal(getAllProductsValue (dtoList));
        order.setBuyerId("BuyerId");
        order.setVendorId("Rozetka");
        order.getProductDTOList().addAll(dtoList);
        orderRepository.save(order);
        log.info("::::: Order {} created", order.getOrderCode());
        return new OrderRespDto(
                order.getId(),
                order.getOrderCode(),
                order.getOrderTotal(),
                dtoList);
    }

    private BigDecimal getAllProductsValue(List<OrderProductDto> dtoList) {
        return dtoList
                .stream()
                .map( dto -> getProductPrice(dto)
                        .multiply(BigDecimal.valueOf(dto.quantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal getProductPrice(OrderProductDto dto) {
        return productRepository
                .findByProductCode(dto.productCode())
                .orElseThrow(() -> new EntityNotFoundException
                        ("Product + " + dto.productCode() + " NOT FOUND"))
                .getProductPrice();
    }
}
