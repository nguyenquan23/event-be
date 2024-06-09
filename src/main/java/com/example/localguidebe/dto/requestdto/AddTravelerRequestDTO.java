package com.example.localguidebe.dto.requestdto;

import com.example.localguidebe.enums.TravelerRequestStatus;
import java.util.List;

public record AddTravelerRequestDTO(
    Long guideId,
    Long travelerRequestId, // can be null
    List<String> transportation,
    Integer duration,
    String unit,
    Double maxPricePerPerson,
    Integer numberOfTravelers,
    String destination,
    String message,
    TravelerRequestStatus travelerRequestStatus) {}
