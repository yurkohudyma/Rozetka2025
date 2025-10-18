package ua.hudyma.controller;

import org.springframework.web.bind.annotation.*;
import ua.hudyma.dto.FilterReqDto;
import ua.hudyma.repository.AttributeRepository;
import ua.hudyma.repository.CategoryRepository;
import ua.hudyma.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log4j2
public class HomeController {

    private final AttributeRepository attributeRepository;
    private final CategoryRepository categoryRepository;
    private final ProductService productService;

    @GetMapping("/")
    public String index(Model model) {
        var productList = productService.getAllSimple();
        var catList = productService.getAllCats();
        model.addAllAttributes(Map.of(
                "productList", productList,
                "showAddProductForm", true,
                "catList", catList));
        return "store";
    }

    @GetMapping("/cat")
    public String getCat (Model model, @RequestParam String catName) {
        var catListProducts = productService.getAllCategoryProducts (catName);
        var attribMap = productService
                .getAttribMapWithDifferentialSorting(catName);
        model.addAllAttributes(Map.of(
                "productList", catListProducts,
                "showAddProductForm", true,
                "catList", productService.getAllCats(),
                "attribMap", attribMap,
                "cat", catName));
        return "store";
    }

    @PostMapping("/filter/{catName}")
    public String filter (Model model, @PathVariable String catName,
                          @RequestBody FilterReqDto filterDto) {
        log.info(filterDto.filters());
        //todo catListProducts = engageFiltering
        var catListProducts = productService.getAllCategoryProducts (catName);
        var attribMap = productService
                .getAttribMapWithDifferentialSorting(catName);
        model.addAllAttributes(Map.of(
                "productList", catListProducts,
                "showAddProductForm", true,
                "catList", productService.getAllCats(),
                "attribMap", attribMap,
                "cat", catName));
        return "fragments/product_list :: productListFragment";
    }
}
