package com.semisvit.controller;

import com.semisvit.dto.ProductReqDto;
import com.semisvit.dto.ProductRespDto;
import com.semisvit.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/products/add")
    public ResponseEntity<ProductRespDto> createProduct (
            @RequestBody ProductReqDto dto){
        return ResponseEntity.ok(productService.createProductWithAttributes(dto));
    }
}
