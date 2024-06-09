package com.example.localguidebe.converter;

import com.example.localguidebe.dto.responsedto.UpdateTourResponseDTO;
import com.example.localguidebe.entity.Tour;
import com.example.localguidebe.enums.AssociateName;
import com.example.localguidebe.service.ImageService;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class TourToUpdateTourResponseDtoConverter {
  private final LocationToLocationDto locationToLocationDto;
  private final TourStartTimeToTourStartTimeDtoConverter tourStartTimeToTourStartTimeDtoConverter;
  private final ImageToImageDtoConverter imageToImageDtoConverter;
  private final ImageService imageService;
  private final CategoryToCategoryDtoConverter categoryToCategoryDtoConverter;

  public TourToUpdateTourResponseDtoConverter(
      LocationToLocationDto locationToLocationDto,
      TourStartTimeToTourStartTimeDtoConverter tourStartTimeToTourStartTimeDtoConverter,
      ImageToImageDtoConverter imageToImageDtoConverter,
      ImageService imageService,
      CategoryToCategoryDtoConverter categoryToCategoryDtoConverter) {
    this.locationToLocationDto = locationToLocationDto;
    this.tourStartTimeToTourStartTimeDtoConverter = tourStartTimeToTourStartTimeDtoConverter;
    this.imageToImageDtoConverter = imageToImageDtoConverter;
    this.imageService = imageService;
    this.categoryToCategoryDtoConverter = categoryToCategoryDtoConverter;
  }

  public UpdateTourResponseDTO convert(Tour source) {
    return new UpdateTourResponseDTO(
        source.getId(),
        source.getName(),
        source.getDescription(),
        source.getTransportation(),
        source.getIncludeService(),
        source.getDuration(),
        source.getUnit(),
        source.getEstimatedLocalCashNeeded(),
        source.getPricePerTraveler(),
        source.getLimitTraveler(),
        source.getItinerary(),
        source.getAddress(),
        source.getTourStartTimes().stream()
            .map(tourStartTimeToTourStartTimeDtoConverter::convert)
            .toList(),
        source.getCategories() != null
            ? source.getCategories().stream()
                .map(categoryToCategoryDtoConverter::convertCategory)
                .collect(Collectors.toSet())
            : null,
        imageService
            .getImageByAssociateIddAndAssociateName(source.getId(), AssociateName.TOUR)
            .stream()
            .map(imageToImageDtoConverter::convert)
            .toList(),
        source.getLocations().stream()
            .map(locationToLocationDto::convert)
            .collect(Collectors.toList()));
  }
}
