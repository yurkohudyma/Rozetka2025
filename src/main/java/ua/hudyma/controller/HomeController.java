package ua.hudyma.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.hudyma.dto.FilterReqDto;
import ua.hudyma.dto.ProductDto;
import ua.hudyma.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import ua.hudyma.service.UserService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

@Controller
@RequiredArgsConstructor
@Log4j2
public class HomeController {
    private final ProductService productService;
    private final UserService userService;

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
        var productsQuantity = productService.getAllProductsQuantity();
        var allProductsValue = productService.getAllProductsValue();
        model.addAllAttributes(Map.of(
                "productList", productList,
                "showFilterPane", false,
                "catList", catList,
                "ordersQuantity", 0,
                "productsQuantity", productsQuantity,
                "usersQuantity", 0,
                "allProductsValue", allProductsValue));
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

    @PostMapping(value = "/edit/{productCode}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String editProduct (@PathVariable String productCode,
                               @ModelAttribute ProductDto dto,
                               @RequestParam MultipartFile[] files) {
        productService.editProduct (productCode, dto, files);
        return "redirect:/";
    }


}
