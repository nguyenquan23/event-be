package com.example.localguidebe.dto.responsedto;

import java.util.List;

public record StatisticOfToursByGuidePaginationDTO(
    List<StatisticalTourDTO> statisticalTourDTOS, Integer totalOfPage, Integer totalOfResult) {}
