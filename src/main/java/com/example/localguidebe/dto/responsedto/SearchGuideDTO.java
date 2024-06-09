package com.example.localguidebe.dto.responsedto;

import com.example.localguidebe.dto.GuideDTO;
import java.util.List;

public record SearchGuideDTO(List<GuideDTO> guides, Integer totalOfPage, Integer totalOfResult) {}
