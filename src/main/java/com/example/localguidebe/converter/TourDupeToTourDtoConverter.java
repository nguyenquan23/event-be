package com.example.localguidebe.converter;

import com.example.localguidebe.dto.*;
import com.example.localguidebe.entity.Image;
import com.example.localguidebe.entity.TourDupe;
import com.example.localguidebe.utils.JsonUtils;
import org.springframework.stereotype.Component;

@Component
public class TourDupeToTourDtoConverter {
  public TourDTO convert(TourDupe tourDupe) throws Exception {
    return new TourDTO(
        tourDupe.getId(),
        tourDupe.getName(),
        tourDupe.getDescription(),
        tourDupe.getTransportation(),
        tourDupe.getIncludeService(),
        tourDupe.getDuration(),
        tourDupe.getUnit(),
        tourDupe.getEstimatedLocalCashNeeded(),
        tourDupe.getPricePerTraveler(),
        tourDupe.getLimitTraveler(),
        tourDupe.getOverallRating() != null ? tourDupe.getOverallRating() : 0.0,
        tourDupe.getItinerary(),
        tourDupe.getIsDeleted(),
        null,
        JsonUtils.convertJsonToObject(tourDupe.getGuide(), UserDTO.class),
        tourDupe.getStatus(),
        JsonUtils.convertJsonToList(tourDupe.getTourStartTimes(), String.class),
        JsonUtils.convertJsonToSet(tourDupe.getCategories(), CategoryDTO.class),
        JsonUtils.convertJsonToList(tourDupe.getReviews(), ReviewDTO.class),
        JsonUtils.convertJsonToList(tourDupe.getImages(), Image.class),
        JsonUtils.convertJsonToList(tourDupe.getLocations(), LocationDTO.class),
        tourDupe.getIsForSpecificTraveler());
  }
}
