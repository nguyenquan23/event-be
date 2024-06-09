package com.example.localguidebe.dto.responsedto;

import java.util.List;

public record SearchSuggestionResponseDTO(
    List<String> addresses, List<ResultInSearchSuggestionDTO> toursOrGuiders) {}
