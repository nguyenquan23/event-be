package com.example.localguidebe.dto.responsedto;

import java.util.List;

public record StatisticalTourPaginationDTO(
    List<StatisticalTourDTO> tourDTOS, Integer totalOfPage, Integer totalOfResult) {}
