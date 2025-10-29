package ua.hudyma.restcontroller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ua.hudyma.repository.CategoryRepository;

@RestController
@RequiredArgsConstructor
public class CategoryRestController {
    private final CategoryRepository categoryRepository;
}
