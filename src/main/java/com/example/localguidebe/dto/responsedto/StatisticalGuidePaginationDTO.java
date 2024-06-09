package com.example.localguidebe.dto.responsedto;

import java.util.List;

public record StatisticalGuidePaginationDTO(
    List<StatisticalGuideDTO> statisticalGuideDTOS, Integer totalOfPage, Integer totalOfResul) {}
