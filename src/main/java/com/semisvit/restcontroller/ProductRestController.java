package com.semisvit.restcontroller;

import com.semisvit.dto.ProductDto;
import com.semisvit.service.ProductService;
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
