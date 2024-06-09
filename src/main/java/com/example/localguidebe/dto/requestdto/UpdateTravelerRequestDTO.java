package com.example.localguidebe.dto.requestdto;

import com.example.localguidebe.enums.TravelerRequestStatus;
import java.util.List;

public record UpdateTravelerRequestDTO(
    List<String> transportation,
    Integer duration,
    String unit,
    Double maxPrice,
    String destination,
    String message,
    TravelerRequestStatus status) {}
