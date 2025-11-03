package ua.hudyma.restcontroller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.hudyma.dto.ProductDto;
import ua.hudyma.service.ProductService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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