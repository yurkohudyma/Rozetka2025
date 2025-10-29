package ua.hudyma.restcontroller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.hudyma.dto.AttribDto;
import ua.hudyma.dto.AttributeDetailsDto;
import ua.hudyma.mapper.AttributeMapper;
import ua.hudyma.repository.AttributeRepository;
import ua.hudyma.service.AttributeService;
import ua.hudyma.service.ProductService;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attributes")
public class AttributeRestController {
    private final AttributeRepository attributeRepository;
    private final AttributeMapper attributeMapper;
    private final AttributeService attributeService;
    private final ProductService productService;

    @GetMapping("/getCatAttribs")
    public List<AttribDto> getCatAttribs (@RequestParam String catName){
        return attributeMapper
                .toDtoList(attributeRepository
                        .findAllByCategoryList_CategoryName(catName));
    }

    @GetMapping
    public AttribDto getAttribByName (@RequestParam String name){
        return attributeMapper
                .toDto(attributeRepository
                        .findByAttributeName(name).orElseThrow());
    }

    @GetMapping("/getCatAttribsWithUnitsFetched")
    public List<AttributeDetailsDto> getCatAttribsFetched (@RequestParam String catName){
        return attributeMapper
                .toAttribDetailsDtoList(attributeRepository
                        .findAllCatAttribsByCategoryNameFetched(catName));
    }

    @GetMapping("/getAllAttribUnits")
    public ResponseEntity<Set<String>> getAllAttribUnits (@RequestParam String attribName){
        return ResponseEntity.ok(attributeService.getAttribUnits(attribName));
    }

    @GetMapping("/details")
    public ResponseEntity<AttributeDetailsDto> getAttributeDetails(
            @RequestParam String name) {
        var details = attributeService.getAttributeDetails(name);
        return ResponseEntity.ok(details);
    }

    @GetMapping("/units")
    public ResponseEntity<Set<String>> getAttribUnits(
            @RequestParam String name) {
        var details = attributeService.getAttribUnits(name);
        return ResponseEntity.ok(details);
    }

    @GetMapping("/values")
    public ResponseEntity<List<String>> getAttribValues(
            @RequestParam String name) {
        var details = attributeService.getAttribValues(name);
        return ResponseEntity.ok(details);
    }

    @GetMapping("/valuesByCatAndName")
    public ResponseEntity<List<String>> getAttribValuesByCatAndAttribName(
            @RequestParam String attribName, @RequestParam String catName) {
        var details = attributeService
                .getAttribValuesByNameAndCatName(attribName, catName);
        return ResponseEntity.ok(details);
    }

    @GetMapping("/getAttribMap")
    public Map<String, Set<String>> getAttribMap (@RequestParam String catName) {
        return productService.getAttribMapWithDifferentialSorting(catName);
    }
}
