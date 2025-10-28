package ua.hudyma.controller;

import org.springframework.web.bind.annotation.*;
import ua.hudyma.dto.FilterReqDto;
import ua.hudyma.dto.ProductDto;
import ua.hudyma.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

@Controller
@RequiredArgsConstructor
@Log4j2
public class HomeController {
    private final ProductService productService;

    @GetMapping("/i18n")
    @ResponseBody
    public Map<String, String> getMessages(@RequestParam(defaultValue = "uk") String lang) {
        Locale locale = Locale.forLanguageTag(lang);
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        Map<String, String> messages = new HashMap<>();
        bundle.keySet().forEach(key -> messages.put(key, bundle.getString(key)));
        log.info(messages);
        return messages;
    }

    @GetMapping("/")
    public String index(Model model) {
        var productList = productService.getAllSimple();
        var catList = productService.getAllCats();
        model.addAllAttributes(Map.of(
                "productList", productList,
                "showFilterPane", false,
                "catList", catList));
        return "store";
    }

    @GetMapping("/cat")
    public String getCat(Model model, @RequestParam String catName) {
        var catListProducts = productService.getAllCategoryProducts(catName);
        var attribMap = productService
                .getAttribMapWithDifferentialSorting(catName);
        var minMaxPriceDto = productService
                .getMinMaxPricesDto(catListProducts);
        model.addAllAttributes(Map.of(
                "productList", catListProducts,
                "catList", productService.getAllCats(),
                "showFilterPane", true,
                "attribMap", attribMap,
                "cat", catName,
                "maxPrice", minMaxPriceDto.maxPrice(),
                "minPrice", minMaxPriceDto.minPrice()));
        return "store";
    }

    @PostMapping("/filter/{catName}")
    public String filter(Model model, @PathVariable String catName,
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
                "showFilterPane", true,
                "showFilterResetButton", true,
                "catList", productService.getAllCats(),
                "attribMap", attribMap,
                "cat", catName));
        return "fragments/product_list :: productListFragment";
    }

    @PostMapping("/products/add")
    public String addProduct(@ModelAttribute ProductDto productDto) {
        productService.createProductWithAttributes(productDto);
        return "redirect:/";
    }

    @DeleteMapping("/products/{productCode}")
    public String deleteProduct (@PathVariable String productCode){
        productService.deleteProduct (productCode);
        return "redirect:/";
    }

    @DeleteMapping("/products")
    public String deleteAllProducts (){
        productService.deleteAllProducts ();
        return "redirect:/";
    }

    @DeleteMapping("/cats")
    public String deleteAllCats (){
        productService.deleteAllCats ();
        return "redirect:/";
    }

    @PatchMapping("/edit/{productCode}")
    public String editProduct (@PathVariable String productCode,
                               @ModelAttribute ProductDto dto){
        productService.editProduct (productCode, dto);
        return "redirect:/";
    }


}
