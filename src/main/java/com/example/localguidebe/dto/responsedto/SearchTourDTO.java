package com.example.localguidebe.dto.responsedto;

import com.example.localguidebe.dto.TourDTO;

import java.util.List;

public record SearchTourDTO (
        List<TourDTO> tourDTOS,
        Integer totalOfPage,
        Integer totalOfResult
){}
