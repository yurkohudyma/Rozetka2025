package ua.hudyma.dto;


import java.util.List;

public record AttributeDetailsDto (
    String attributeName,
    String attributeValue,
    String attributeType,
    List<String> units) {
}

