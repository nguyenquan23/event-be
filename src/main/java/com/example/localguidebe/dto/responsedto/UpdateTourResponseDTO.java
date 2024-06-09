package com.example.localguidebe.dto.responsedto;

import com.example.localguidebe.dto.CategoryDTO;
import com.example.localguidebe.dto.ImageDTO;
import com.example.localguidebe.dto.LocationDTO;
import com.example.localguidebe.dto.TourStartTimeDTO;
import java.util.List;
import java.util.Set;

public record UpdateTourResponseDTO(
    Long id,
    String name,
    String description,
    String transportation,
    String includeService,
    Integer duration,
    String unit,
    String estimatedLocalCashNeeded,
    Double pricePerTraveler,
    Integer limitTraveler,
    String itinerary,
    String province,
    List<TourStartTimeDTO> tourStartTimes,
    Set<CategoryDTO> categories,
    List<ImageDTO> images,
    List<LocationDTO> locations) {}
