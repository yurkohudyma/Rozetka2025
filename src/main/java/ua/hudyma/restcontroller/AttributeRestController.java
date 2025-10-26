package ua.hudyma.restcontroller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.hudyma.dto.AttribDto;
import ua.hudyma.mapper.AttributeMapper;
import ua.hudyma.repository.AttributeRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attributes")
public class AttributeRestController {
    private final AttributeRepository attributeRepository;
    private final AttributeMapper attributeMapper;

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
}
