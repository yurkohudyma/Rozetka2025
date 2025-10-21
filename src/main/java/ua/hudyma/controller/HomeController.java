package ua.hudyma.controller;

import org.springframework.web.bind.annotation.*;
import ua.hudyma.dto.FilterReqDto;
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
    private final ProductService productService;

    @GetMapping("/")
    public String index(Model model) {
        var productList = productService.getAllSimple();
        var catList = productService.getAllCats();
        model.addAllAttributes(Map.of(
                "productList", productList,
                "showAddProductForm", true,
                "showFilterPane", false,
                "catList", catList));
        return "store";
    }

    @GetMapping("/cat")
    public String getCat (Model model, @RequestParam String catName) {
        var catListProducts = productService.getAllCategoryProducts (catName);
        var attribMap = productService
                .getAttribMapWithDifferentialSorting(catName);
        var minMaxPriceDto = productService
                .getMinMaxPricesDto(catListProducts);
        model.addAllAttributes(Map.of(
                "productList", catListProducts,
                "showAddProductForm", true,
                "catList", productService.getAllCats(),
                "showFilterPane", true,
                "attribMap", attribMap,
                "cat", catName,
                "maxPrice", minMaxPriceDto.maxPrice(),
                "minPrice", minMaxPriceDto.minPrice()));
        return "store";
    }

    @PostMapping("/filter/{catName}")
    public String filter (Model model, @PathVariable String catName,
                          @RequestBody FilterReqDto filterDto) {
        var filterMap = filterDto.filterMap();
        var selectedMinPrice = filterDto.minPrice();
        var selectedMaxPrice = filterDto.maxPrice();
        var getCatFilteredProducts = productService
                .getCatFilteredProducts(
                        catName,
                        filterMap,
                        selectedMaxPrice,
                        selectedMinPrice);
        var attribMap = productService
                .getAttribMapWithDifferentialSorting(catName);
        model.addAllAttributes(Map.of(
                "productList", getCatFilteredProducts,
                "showAddProductForm", true,
                "showFilterPane", true,
                "showFilterResetButton", true,
                "catList", productService.getAllCats(),
                "attribMap", attribMap,
                "cat", catName));
        return "fragments/product_list :: productListFragment";
    }
}
