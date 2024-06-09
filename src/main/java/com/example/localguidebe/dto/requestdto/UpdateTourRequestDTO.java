package com.example.localguidebe.dto.requestdto;

import com.example.localguidebe.entity.Location;
import com.example.localguidebe.entity.TourStartTime;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record UpdateTourRequestDTO(
    Long id,
    String name,
    String description,
    String transportation,
    LocalDateTime includeService,
    Integer duration,
    String unit,
    String estimatedLocalCashNeeded,
    Double pricePerTraveler,
    Integer limitTraveler,
    String address,
    String itinerary,
    Integer guide_id,
    List<TourStartTime> tourStartTimes,
    List<Long> category_ids,
    List<Object> images,
    List<Location> locations,
    Location meetingPoint) {}
