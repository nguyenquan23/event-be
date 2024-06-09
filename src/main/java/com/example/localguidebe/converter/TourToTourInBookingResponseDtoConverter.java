package com.example.localguidebe.converter;

import com.example.localguidebe.dto.responsedto.TourInBookingResponseDTO;
import com.example.localguidebe.entity.Tour;
import com.example.localguidebe.enums.AssociateName;
import com.example.localguidebe.service.ImageService;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class TourToTourInBookingResponseDtoConverter {
  private final ImageService imageService;
  private final LocationToLocationDtoConverter locationToLocationDtoConverter;

  public TourToTourInBookingResponseDtoConverter(
      ImageService imageService, LocationToLocationDtoConverter locationToLocationDtoConverter) {
    this.imageService = imageService;
    this.locationToLocationDtoConverter = locationToLocationDtoConverter;
  }

  public TourInBookingResponseDTO convert(Tour source) {
    return new TourInBookingResponseDTO(
        source.getId(),
        source.getName(),
        source.getOverallRating(),
        imageService.getImageByAssociateIddAndAssociateName(source.getId(), AssociateName.TOUR),
        source.getLocations().stream()
            .map(locationToLocationDtoConverter::convert)
            .collect(Collectors.toSet()));
  }
}
