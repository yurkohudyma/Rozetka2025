package ua.hudyma.dto;

import java.util.List;
import java.util.Map;

public record FilterReqDto(
        Map<String, List<String>> filterMap) {}
