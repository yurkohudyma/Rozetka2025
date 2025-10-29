package ua.hudyma.restcontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.hudyma.domain.orders.OrderProductDto;
import ua.hudyma.domain.orders.OrderRespDto;
import ua.hudyma.service.OrderService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Log4j2
public class OrderRestController {
    private final OrderService orderService;

    @PostMapping("/add") public ResponseEntity<OrderRespDto> createOrder (
            @RequestBody List<OrderProductDto> dto){
        return ResponseEntity.ok(orderService.createOrder (dto));
    }
}
