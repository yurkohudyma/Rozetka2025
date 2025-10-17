package ua.hudyma.restcontroller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.hudyma.dto.ProductDto;
import ua.hudyma.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class ProductRestController {
    private final ProductService productService;

    @PostMapping("/products/add")
    public ResponseEntity<ProductDto> createProduct (
            @RequestBody ProductDto dto){
        return ResponseEntity.ok(productService.createProductWithAttributes(dto));
    }

    @GetMapping("/getAttribMap")
    public Map<String, Set<String>> getAttribMap (Model model, @RequestParam String catName) {
        return productService.getAttribNamesAndValuesListMapPerCatImperative(catName);
    }
}
