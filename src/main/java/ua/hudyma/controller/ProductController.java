package ua.hudyma.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import ua.hudyma.domain.Attribute;
import ua.hudyma.domain.Product;
import ua.hudyma.mapper.ProductMapper;
import ua.hudyma.service.ProductService;

@Controller
@RequiredArgsConstructor
@Log4j2
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;
   /* @PostMapping("/products/add")
    public String createProduct (Product product,
                                 String catName,
                                 Attribute[] attributes){
        productService
                .createProductWithAttributes(product, catName, attributes);
        return "redirect:/products";
    }*/
}
