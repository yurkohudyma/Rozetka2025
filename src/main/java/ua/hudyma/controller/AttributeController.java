package ua.hudyma.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.hudyma.dto.AttributeDetailsDto;
import ua.hudyma.service.AttributeService;

@RestController
@RequestMapping("/attributes")
@RequiredArgsConstructor
public class AttributeController {

    private final AttributeService attributeService;

    @GetMapping("/details")
    public ResponseEntity<AttributeDetailsDto> getAttributeDetails(
            @RequestParam String name) {
        var details = attributeService.getAttributeDetails(name);
        return ResponseEntity.ok(details);
    }
}

