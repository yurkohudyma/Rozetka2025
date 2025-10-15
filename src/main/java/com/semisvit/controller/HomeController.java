package com.semisvit.controller;

import com.semisvit.repository.AttributeRepository;
import com.semisvit.repository.CategoryRepository;
import com.semisvit.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Log4j2
public class HomeController {

    private final AttributeRepository attributeRepository;
    private final CategoryRepository categoryRepository;
    private final ProductService productService;

    @GetMapping("/")
    @Transactional
    public String index(Model model) {
        /*var pageble = PageRequest.of(0, 6, Sort.by("productCode").ascending());*/
        var productList = productService.getAllProductDtos();
        /*productList.forEach(p -> Hibernate.initialize(p.getProductPropertiesList()));*/
        model.addAttribute("productList", productList);
        model.addAttribute("showAddProductForm", true);
        return "store";
    }
}
