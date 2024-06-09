package com.example.localguidebe.converter;

import com.example.localguidebe.dto.LocationDTO;
import com.example.localguidebe.dto.TourDTO;
import com.example.localguidebe.entity.Tour;
import com.example.localguidebe.enums.AssociateName;
import com.example.localguidebe.enums.TourStatusEnum;
import com.example.localguidebe.service.ImageService;
import java.util.Comparator;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TourToTourDtoConverter {
  private final ImageToImageDtoConverter imageToImageDtoConverter;
  private final UserToUserDtoConverter userToUserDtoConverter;

  private final CategoryToCategoryDtoConverter categoryToCategoryDtoConverter;
  private final ImageService imageService;
  private final LocationToLocationDtoConverter locationToLocationDtoConverter;
  private final ReviewToReviewDtoConverter reviewToReviewDtoConverter;
  private final TourStartTimeToTourStartTimeDtoConverter tourStartTimeToTourStartTimeDtoConverter;

  @Autowired
  public TourToTourDtoConverter(
      ImageToImageDtoConverter imageToImageDtoConverter,
      UserToUserDtoConverter userToUserDtoConverter,
      CategoryToCategoryDtoConverter categoryToCategoryDtoConverter,
      ImageService imageService,
      LocationToLocationDtoConverter locationToLocationDtoConverter,
      ReviewToReviewDtoConverter reviewToReviewDtoConverter,
      TourStartTimeToTourStartTimeDtoConverter tourStartTimeToTourStartTimeDtoConverter) {
    this.imageToImageDtoConverter = imageToImageDtoConverter;
    this.userToUserDtoConverter = userToUserDtoConverter;
    this.categoryToCategoryDtoConverter = categoryToCategoryDtoConverter;
    this.imageService = imageService;
    this.locationToLocationDtoConverter = locationToLocationDtoConverter;
    this.reviewToReviewDtoConverter = reviewToReviewDtoConverter;
    this.tourStartTimeToTourStartTimeDtoConverter = tourStartTimeToTourStartTimeDtoConverter;
  }

  public TourDTO convert(Tour tour) {
    return new TourDTO(
        tour.getId(),
        tour.getName(),
        tour.getDescription(),
        tour.getTransportation(),
        tour.getIncludeService(),
        tour.getDuration(),
        tour.getUnit(),
        tour.getEstimatedLocalCashNeeded(),
        tour.getPricePerTraveler(),
        tour.getLimitTraveler(),
        tour.getOverallRating() != null ? tour.getOverallRating() : 0.0,
        tour.getItinerary(),
        tour.getIsDeleted(),
        tour.getAddress(),
        tour.getGuide() != null ? userToUserDtoConverter.convert(tour.getGuide()) : null,
        tour.getStatus() != null ? tour.getStatus() : TourStatusEnum.PENDING,
        tour.getTourStartTimes().stream()
            .map(tourStartTime -> tourStartTime.getStartTime().toString())
            .toList(),
        tour.getCategories() != null
            ? tour.getCategories().stream()
                .map(categoryToCategoryDtoConverter::convertCategory)
                .collect(Collectors.toSet())
            : null,
        tour.getReviews().stream()
            .map(reviewToReviewDtoConverter::convert)
            .collect(Collectors.toList()),
        imageService.getImageByAssociateIddAndAssociateName(tour.getId(), AssociateName.TOUR),
        tour.getLocations().stream()
            .map(locationToLocationDtoConverter::convert)
            .sorted(Comparator.comparing(LocationDTO::id))
            .collect(Collectors.toList()),
        tour.getIsForSpecificTraveler());
  }
}
