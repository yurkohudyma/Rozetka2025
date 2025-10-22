package ua.hudyma.restcontroller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.hudyma.dto.AttribDto;
import ua.hudyma.mapper.AttributeMapper;
import ua.hudyma.repository.AttributeRepository;
import ua.hudyma.repository.CategoryRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AttributeRestController {
    private final AttributeRepository attributeRepository;
    private final AttributeMapper attributeMapper;

    @GetMapping("/attributes/getCatAttribs")
    public List<AttribDto> getCatAttribs (@RequestParam String catName){
        return attributeMapper
                .toDtoList(attributeRepository
                        .findAllByCategory_CategoryName(catName));
    }

    //todo вирішити проблему перевикористання атрибутів для різних категорій
    // ==> Cat <-> Attrib ManyToMany
}
