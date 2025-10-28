package ua.hudyma.dto;


import java.util.List;
import java.util.Set;

public record AttributeDetailsDto (
    String attributeName,
    String attributeValue,
    String attributeType,
    Set<String> units) {
}

