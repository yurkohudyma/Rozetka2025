package ua.hudyma.restcontroller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.hudyma.dto.ProductDto;
import ua.hudyma.service.ProductService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductRestController {
    private final ProductService productService;

    @PostMapping("/products/add")
    public ResponseEntity<ProductDto> createProduct (
            @RequestBody ProductDto dto){
        return ResponseEntity.ok(productService.createProductWithAttributes(dto));
    }

    @GetMapping("/getVendorName")
    public ResponseEntity<Map<String, String>> getVendorName (@RequestParam String vendorCode){
        var name = productService.getVendorNameByCode (vendorCode);
        return ResponseEntity.ok(Map.of("name", name));
    }




}