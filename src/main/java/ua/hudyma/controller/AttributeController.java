package ua.hudyma.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.hudyma.dto.AttributeDetailsDto;
import ua.hudyma.service.AttributeService;

import java.util.List;
import java.util.Set;

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
}

