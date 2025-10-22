package ua.hudyma.restcontroller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.hudyma.dto.AttribDto;
import ua.hudyma.repository.CategoryRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryRestController {
    private final CategoryRepository categoryRepository;

    @GetMapping("/getCatAttribs")
    public List<AttribDto> getCatAttribs (@RequestParam String catName){
        throw new IllegalArgumentException("Method not IMPLEM");
    }
}
