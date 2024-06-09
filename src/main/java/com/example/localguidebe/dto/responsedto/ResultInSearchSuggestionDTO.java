package com.example.localguidebe.dto.responsedto;

/** The returned result could be either a tour or a guide */
public record ResultInSearchSuggestionDTO(Long id, String resultName, String imageLink) {}
