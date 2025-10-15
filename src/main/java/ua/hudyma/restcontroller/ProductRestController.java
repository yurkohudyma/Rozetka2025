package ua.hudyma.restcontroller;

import ua.hudyma.dto.ProductDto;
import ua.hudyma.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductRestController {
    private final ProductService productService;

    @PostMapping("/products/add")
    public ResponseEntity<ProductDto> createProduct (
            @RequestBody ProductDto dto){
        return ResponseEntity.ok(productService.createProductWithAttributes(dto));
    }
}
