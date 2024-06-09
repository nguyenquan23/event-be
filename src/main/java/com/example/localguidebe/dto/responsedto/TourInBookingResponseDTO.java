package com.example.localguidebe.dto.responsedto;

import com.example.localguidebe.dto.LocationDTO;
import com.example.localguidebe.entity.Image;
import java.util.List;
import java.util.Set;

public record TourInBookingResponseDTO(
    Long id, String name, Double overallRating, List<Image> images, Set<LocationDTO> locations) {}
