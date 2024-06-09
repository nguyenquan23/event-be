package com.example.localguidebe.dto;

import com.example.localguidebe.enums.TravelerRequestStatus;
import java.util.List;

public record TravelerRequestDTO(
    Long id,
    List<String> transportation,
    Integer duration,
    String unit,
    Double maxPricePerPerson,
    Integer numberOfTravelers,
    String destination,
    String message,
    TravelerRequestStatus status,
    GuideDTO guide,
    UserDTO traveler,
    TourDTO tour) {}
