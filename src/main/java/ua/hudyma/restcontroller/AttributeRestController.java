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

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attributes")
public class AttributeRestController {
    private final AttributeRepository attributeRepository;
    private final AttributeMapper attributeMapper;
    private final AttributeService attributeService;

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
}
