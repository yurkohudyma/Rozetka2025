package ua.hudyma.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.hudyma.domain.orders.*;
import ua.hudyma.exception.DtoObligatoryFieldsAreMissingException;
import ua.hudyma.mapper.OrderMapper;
import ua.hudyma.repository.BuyerRepository;
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
    private final OrderMapper orderMapper;
    private final BuyerRepository buyerRepository;

    public OrderRespDto createOrder(List<OrderProductDto> dtoList, String buyerCode) {
        if (dtoList == null || dtoList.isEmpty() || buyerCode == null || buyerCode.isEmpty()) {
            throw new DtoObligatoryFieldsAreMissingException("dtoList or buyerCode is NULL or EMPTY");
        }
        if (!buyerRepository.existsByBuyerCode(buyerCode)){
            throw new EntityNotFoundException("Buyer "+ buyerCode + " NOT FOUND");
        }
        checkProductsAvailability(dtoList);
        var order = new Order();
        order.setOrderStatus(OrderStatus.CONFIRMED);
        order.setOrderTotal(getAllProductsValue (dtoList));
        order.setBuyerCode(buyerCode);
        var productDTOList = order.getProductDTOList();
        productDTOList.addAll(dtoList);
        orderRepository.save(order);
        log.info("::::: Order {} created", order.getOrderCode());
        return orderMapper.toDto(order);
    }

    @Transactional
    public OrderRespDto updateOrderBuyer(String buyerCode, String orderCode) {
        if (!buyerRepository.existsByBuyerCode(buyerCode)){
            throw new EntityNotFoundException("Vendor " + buyerCode + " NOT FOUND");
        }
        var order = orderRepository
                .findByOrderCode(orderCode)
                .orElseThrow( () -> new EntityNotFoundException("Order "+ orderCode + " DOES NOT exist"));
        order.setBuyerCode(buyerCode);
        return orderMapper.toDto(order);
    }

    private void checkProductsAvailability(List<OrderProductDto> dtoList) {
        for (OrderProductDto dto: dtoList){
            if (!productRepository.existsByProductCode(dto.productCode())){
                throw new EntityNotFoundException("Product "+ dto.productCode() + " DOES NOT exist");
            }
        }
    }

    public OrderRespDto getOrder(String orderCode) {
        var order = orderRepository.findByOrderCode(orderCode).orElseThrow(
                () -> new EntityNotFoundException
                ("Order " + orderCode + " NOT FOUND"));
        return orderMapper.toDto(order);
    }

    private BigDecimal getAllProductsValue(List<OrderProductDto> dtoList) {
        return dtoList.stream()
                .map(dto -> getProductPrice(dto)
                        .multiply(BigDecimal.valueOf(dto.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getProductPrice(OrderProductDto dto) {
        return productRepository
                .findByProductCode(dto.productCode())
                .orElseThrow()
                .getProductPrice();
    }
}
