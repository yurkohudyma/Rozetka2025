package ua.hudyma.restcontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/add")
    public ResponseEntity<OrderRespDto> createOrder (
            @RequestBody List<OrderProductDto> dto,
            @RequestParam String buyerCode){
        return ResponseEntity.ok(orderService.createOrder (dto, buyerCode));
    }

    @GetMapping("/{orderCode}")
    public ResponseEntity<OrderRespDto> getOrder (@PathVariable String orderCode){
        return ResponseEntity.ok(orderService.getOrder (orderCode));
    }

    @PatchMapping("/updateBuyer")
    public ResponseEntity<OrderRespDto> updateOrderBuyer (
            @RequestParam String buyerCode, @RequestParam String orderCode){
        return ResponseEntity.ok(orderService.updateOrderBuyer (buyerCode, orderCode));
    }
}
