package com.example.localguidebe.service.impl;

import com.example.localguidebe.converter.ReviewToReviewResponseDto;
import com.example.localguidebe.converter.ToResultInSearchSuggestionDtoConverter;
import com.example.localguidebe.converter.TourToTourDtoConverter;
import com.example.localguidebe.dto.LocationDTO;
import com.example.localguidebe.dto.TourDTO;
import com.example.localguidebe.dto.requestdto.InfoLocationDTO;
import com.example.localguidebe.dto.requestdto.TourRequestDTO;
import com.example.localguidebe.dto.requestdto.UpdateTourRequestDTO;
import com.example.localguidebe.dto.responsedto.ResultInSearchSuggestionDTO;
import com.example.localguidebe.dto.responsedto.ReviewResponseDTO;
import com.example.localguidebe.dto.responsedto.SearchSuggestionResponseDTO;
import com.example.localguidebe.dto.responsedto.SearchTourDTO;
import com.example.localguidebe.entity.*;
import com.example.localguidebe.enums.*;
import com.example.localguidebe.exception.ConvertTourToTourDupeException;
import com.example.localguidebe.repository.*;
import com.example.localguidebe.service.*;
import com.example.localguidebe.system.constants.NotificationMessage;
import com.example.localguidebe.utils.AddressUtils;
import com.example.localguidebe.utils.CloudinaryUtil;
import com.google.gson.Gson;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TourServiceImpl implements TourService {
  Logger logger = LoggerFactory.getLogger(TourServiceImpl.class);
  private final TourToTourDtoConverter tourToTourDtoConverter;
  private final ToResultInSearchSuggestionDtoConverter toResultInSearchSuggestionDtoConverter;
  private final TourStartTimeRepository tourStartTimeRepository;
  private final ImageRepository imageRepository;
  private TourRepository tourRepository;
  private final CategoryService categoryService;
  private final TourStartTimeService tourStartTimeService;
  private final LocationService locationService;
  private final UserService userService;
  private final CloudinaryUtil cloudinaryUtil;
  private final GeoCodingService geoCodingService;
  private final BookingRepository bookingRepository;
  private final ReviewToReviewResponseDto reviewToReviewResponseDto;
  private final Gson gson = new Gson();
  private final NotificationService notificationService;
  private final CartRepository cartRepository;
  private final ReviewRepository reviewRepository;
  private final TourDupeService tourDupeService;

  @Autowired
  public TourServiceImpl(
      ToResultInSearchSuggestionDtoConverter toResultInSearchSuggestionDtoConverter,
      TourStartTimeRepository tourStartTimeRepository,
      CategoryService categoryService,
      TourStartTimeService tourStartTimeService,
      LocationService locationService,
      UserService userService,
      CloudinaryUtil cloudinaryUtil,
      ImageRepository imageRepository,
      BookingRepository bookingRepository,
      TourToTourDtoConverter tourToTourDtoConverter,
      GeoCodingService geoCodingService,
      ReviewToReviewResponseDto reviewToReviewResponseDto,
      NotificationService notificationService,
      CartRepository cartRepository,
      ReviewRepository reviewRepository,
      TourDupeService tourDupeService) {
    this.tourStartTimeRepository = tourStartTimeRepository;
    this.toResultInSearchSuggestionDtoConverter = toResultInSearchSuggestionDtoConverter;
    this.geoCodingService = geoCodingService;
    this.categoryService = categoryService;
    this.tourStartTimeService = tourStartTimeService;
    this.locationService = locationService;
    this.userService = userService;
    this.cloudinaryUtil = cloudinaryUtil;
    this.imageRepository = imageRepository;
    this.tourToTourDtoConverter = tourToTourDtoConverter;
    this.bookingRepository = bookingRepository;
    this.reviewToReviewResponseDto = reviewToReviewResponseDto;
    this.notificationService = notificationService;
    this.cartRepository = cartRepository;
    this.reviewRepository = reviewRepository;
    this.tourDupeService = tourDupeService;
  }

  @Autowired
  public void setTourRepository(TourRepository tourRepository) {
    this.tourRepository = tourRepository;
  }

  @Override
  public SearchTourDTO getListTour(Integer page, Integer limit) {
    Sort sort = Sort.by("overallRating").descending();
    Pageable paging = PageRequest.of(page, limit, sort);
    Page<Tour> tourPage = tourRepository.getListTours(paging);
    List<TourDTO> tourDTOS =
        tourPage.get().map(tourToTourDtoConverter::convert).collect(Collectors.toList());
    return new SearchTourDTO(tourDTOS, tourPage.getTotalPages(), (int) tourPage.getTotalElements());
  }

  @Override
  public SearchTourDTO getListTourForAdmin(Integer page, Integer limit) {
    Sort sort = Sort.by("overallRating").descending();
    Pageable paging = PageRequest.of(page, limit, sort);
    Page<Tour> tourPage = tourRepository.findAll(paging);
    List<TourDTO> tourDTOS =
        tourPage.get().map(tourToTourDtoConverter::convert).collect(Collectors.toList());
    return new SearchTourDTO(tourDTOS, tourPage.getTotalPages(), (int) tourPage.getTotalElements());
  }

  @Override
  public Tour saveTour(TourRequestDTO tourRequestDTO, String email) {
    Tour newTour = new Tour();
    User guide = userService.findUserByEmail(email);
    Gson gson = new Gson();
    BeanUtils.copyProperties(tourRequestDTO, newTour, "categories", "images", "locations");

    // add tour start time
    tourRequestDTO
        .getStartTimes()
        .forEach(
            getStartTime ->
                newTour
                    .getTourStartTimes()
                    .add(TourStartTime.builder().startTime(getStartTime).tour(newTour).build()));
    newTour.setStatus(TourStatusEnum.ACCEPT);
    if (tourRequestDTO.getLocations().size() != 0) {
      tourRequestDTO
          .getLocations()
          .forEach(
              locationDTO -> {
                InfoLocationDTO infoLocationDTO =
                    gson.fromJson(
                        geoCodingService.getAddress(
                            locationDTO.latitude(), locationDTO.longitude()),
                        InfoLocationDTO.class);
                newTour
                    .getLocations()
                    .add(
                        Location.builder()
                            .address(
                                AddressUtils.removeVietnameseAccents(
                                    infoLocationDTO.getDisplay_name()))
                            .name(locationDTO.name())
                            .latitude(locationDTO.latitude())
                            .longitude(locationDTO.longitude())
                            .build());
              });
    }
    // add guider for tour
    tourRequestDTO
        .getCategories()
        .forEach(
            category ->
                newTour.getCategories().add(categoryService.getCategoryById(category.getId())));
    if (guide != null) {
      newTour.setGuide(guide);
    }
    Tour tour = tourRepository.save(newTour);
    tourRequestDTO
        .getImages()
        .forEach(
            image -> {
              Image imageTour = new Image();
              try {
                imageTour.setImageLink(cloudinaryUtil.uploadFile(image, FolderName.tour));
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
              imageTour.setAssociateId(tour.getId());
              imageTour.setAssociateName(AssociateName.TOUR);
              imageRepository.save(imageTour);
            });
    assert guide != null;
    notificationService.addNotification(
        guide.getId(),
        null,
        tour.getId(),
        NotificationTypeEnum.ADD_TOUR,
        NotificationMessage.ADD_TOUR);
    return tourRepository.save(tour);
  }

  @Transactional
  @Override
  public Tour updateTour(UpdateTourRequestDTO updateTourRequestDTO) {
    Optional<Tour> optionalTour = tourRepository.findById(updateTourRequestDTO.id());
    if (optionalTour.isPresent()) {
      Tour tour = optionalTour.get();
      BeanUtils.copyProperties(
          updateTourRequestDTO, tour, "categories", "tourStartTimes", "meetingPoint", "locations");

      // Update tour start time
      if (updateTourRequestDTO.tourStartTimes() != null) {
        tour.getTourStartTimes()
            .forEach(tourStartTime -> tourStartTimeService.deleteById(tourStartTime.getId()));
        tour.getTourStartTimes().clear();
        updateTourRequestDTO
            .tourStartTimes()
            .forEach(
                tourStartTime -> {
                  tourStartTime.setTour(tour);
                  tour.getTourStartTimes().add(tourStartTime);
                });
        logger.info("updated tour start time");
      }

      // Update category
      if (updateTourRequestDTO.category_ids() != null) {
        boolean isUpdateCategory =
            !tour.getCategories().stream()
                .map(Category::getId)
                .sorted()
                .toList()
                .equals(
                    updateTourRequestDTO.category_ids().stream()
                        .sorted()
                        .collect(Collectors.toList()));
        if (isUpdateCategory) {
          tour.getCategories().clear();
          updateTourRequestDTO
              .category_ids()
              .forEach(
                  categoryId ->
                      tour.getCategories().add(categoryService.getCategoryById(categoryId)));

          logger.info("updated category");
        }
      }

      // Update images
      List<Image> tourImages =
          imageRepository.getImageByAssociateIddAndAssociateName(
              updateTourRequestDTO.id(), AssociateName.TOUR);
      int maxSize = Math.max(tourImages.size(), updateTourRequestDTO.images().size());
      for (int i = 0; i < maxSize; i++) {
        if (i < updateTourRequestDTO.images().size()
            // If object is Base64 String
            && !(updateTourRequestDTO.images().get(i) instanceof Image)) {
          Image imageTour = new Image();
          String imageBase64 = (String) updateTourRequestDTO.images().get(i);
          try {
            imageTour.setImageLink(cloudinaryUtil.uploadFile(imageBase64, FolderName.tour));
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
          imageTour.setAssociateId(tour.getId());
          imageTour.setAssociateName(AssociateName.TOUR);
          imageRepository.save(imageTour);
          logger.info("added new image");
        }

        // If object is Image
        if (i < tourImages.size() && updateTourRequestDTO.images().get(i) instanceof Image image) {
          // If image is deleted
          if (!updateTourRequestDTO.images().contains(image.getId().toString())) {
            if (cloudinaryUtil.deleteFile(tourImages.get(i).getImageLink(), FolderName.tour)) {
              imageRepository.deleteById(tourImages.get(i).getId());
              logger.info("deleted image");
            }
          }
        }
      }

      // Update locations
      if (updateTourRequestDTO.locations() != null) {
        tour.getLocations().clear();
        updateTourRequestDTO
            .locations()
            .forEach(
                location -> {
                  if (location.getId() != null)
                    tour.getLocations().add(locationService.findById(location.getId()));
                  else {
                    InfoLocationDTO infoLocationDTO =
                        gson.fromJson(
                            geoCodingService.getAddress(
                                location.getLatitude(), location.getLongitude()),
                            InfoLocationDTO.class);
                    tour.getLocations()
                        .add(
                            Location.builder()
                                .address(
                                    AddressUtils.removeVietnameseAccents(
                                        infoLocationDTO.getDisplay_name()))
                                .name(location.getName())
                                .latitude(location.getLatitude())
                                .longitude(location.getLongitude())
                                .build());
                  }
                });
        logger.info("updated location");
      }
      Tour tourBeforeAddTourDupe = tourRepository.save(tour);
      try {
        tourDupeService.addTourDupe(tourBeforeAddTourDupe);
      } catch (Exception e) {
        throw new ConvertTourToTourDupeException(e.getMessage());
      }
      return tourBeforeAddTourDupe;
    }
    return null;
  }

  @Override
  public TourDTO getTourById(Long id) {
    return tourToTourDtoConverter.convert(tourRepository.findById(id).orElseThrow());
  }

  @Override
  public SearchTourDTO getTours(
      Integer page,
      Integer limit,
      String sortBy,
      String order,
      String searchKey,
      Double minPrice,
      Double maxPrice,
      List<Long> categoryId) {
    Sort sort = order.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    Pageable paging = PageRequest.of(page, limit, sort);
    Page<Tour> tourPage =
        tourRepository.findTours(searchKey, minPrice, maxPrice, categoryId, paging);
    List<TourDTO> tourDTOS =
        tourPage.get().map(tourToTourDtoConverter::convert).collect(Collectors.toList());
    return new SearchTourDTO(tourDTOS, tourPage.getTotalPages(), (int) tourPage.getTotalElements());
  }

  @Override
  public List<TourDTO> deleteTour(Long id) {
    Tour tour = tourRepository.findById(id).orElseThrow();
    tour.setIsDeleted(true);
    tourRepository.save(tour);
    return tourRepository.findAll().stream()
        .map(tourToTourDtoConverter::convert)
        .collect(Collectors.toList());
  }

  @Override
  public List<Tour> getToursOfGuide(Long guideId) {
    return tourRepository.findAll().stream()
        .filter(
            tour -> tour.getIsDeleted().equals(false) && tour.getGuide().getId().equals(guideId))
        .collect(Collectors.toList());
  }

  @Override
  public SearchSuggestionResponseDTO getTourAndTourLocations(String searchValue) {
    List<Tour> tours = tourRepository.findAll();
    List<String> addressesFiltered =
        tours.stream()
            .flatMap(tour -> tour.getLocations().stream().map(Location::getAddress))
            .filter(
                address -> {
                  if (address == null) return false;
                  return AddressUtils.removeVietnameseAccents(address)
                      .toLowerCase()
                      .contains(AddressUtils.removeVietnameseAccents(searchValue).toLowerCase());
                })
            .distinct()
            .toList();
    List<ResultInSearchSuggestionDTO> guidersFiltered =
        tours.stream()
            .sorted(Comparator.comparing(Tour::getOverallRating).reversed())
            .filter(
                tour -> {
                  if (tour.getName() == null) return false;
                  return AddressUtils.removeVietnameseAccents(tour.getName())
                      .toLowerCase()
                      .contains(AddressUtils.removeVietnameseAccents(searchValue).toLowerCase());
                })
            .map(toResultInSearchSuggestionDtoConverter::convert)
            .toList();
    return new SearchSuggestionResponseDTO(addressesFiltered, guidersFiltered);
  }

  @Override
  public List<String> getTourStartTimeAvailable(Long tourId, LocalDate localDate) {
    List<String> tourStartTimes = tourStartTimeRepository.findByTourId(tourId);
    List<String> startDateTimesInBooKing =
        bookingRepository.findStartDateTimesByTourIdAndStartDate(tourId, localDate);
    List<String> tourStartTimesAvailable = new ArrayList<>(tourStartTimes);
    tourStartTimesAvailable.removeAll(startDateTimesInBooKing);
    return tourStartTimesAvailable;
  }

  @Override
  public boolean checkBookingByTraveler(Long tourId, String email) {
    User traveler = userService.findUserByEmail(email);
    return bookingRepository.findAll().stream()
        .anyMatch(
            booking ->
                Objects.equals(booking.getTour().getId(), tourId)
                    && Objects.equals(booking.getCart().getTraveler().getId(), traveler.getId())
                    && booking.getStatus().equals(BookingStatusEnum.PAID));
  }

  @Override
  public boolean checkExistingReviewsByTraveler(Long travelerId, Long tourId) {
    int SuccessBookingNumber = cartRepository.getSuccessBookingNumber(tourId, travelerId);
    int ReviewNumber = reviewRepository.getReviewNumber(travelerId, tourId);
    if (SuccessBookingNumber <= ReviewNumber) {
      return false;
    }
    return true;
  }

  @Override
  public void updateRatingForTour(Tour tour) {
    tour.setOverallRating(
        tour.getReviews().stream()
            .map(Review::getRating)
            .filter(rating -> rating > 0)
            .mapToInt(Integer::intValue)
            .average()
            .orElse(0.0));
    tourRepository.save(tour);
  }

  @Override
  public List<String> getLocationName(List<LocationDTO> locationDTOS) {
    List<String> locationName = new ArrayList<>();
    locationDTOS.forEach(
        location ->
            locationName.add(
                gson.fromJson(
                        geoCodingService.getAddress(location.latitude(), location.longitude()),
                        InfoLocationDTO.class)
                    .getName()));
    return locationName;
  }

  @Override
  public List<ReviewResponseDTO> filterReviewForTour(
      List<Integer> ratings, Long tourId, String sortBy) {
    if (ratings.size() == 0) {
      ratings = Arrays.asList(1, 2, 3, 4, 5);
    }
    return tourRepository.filterReviewForTour(ratings, tourId, sortBy).stream()
        .map(reviewToReviewResponseDto::convert)
        .collect(Collectors.toList());
  }

  @Override
  public TourDTO acceptTour(Long tourId) {
    Tour tour = tourRepository.findById(tourId).orElseThrow();
    tour.setStatus(TourStatusEnum.ACCEPT);
    return tourToTourDtoConverter.convert(tourRepository.save(tour));
  }

  @Override
  public TourDTO denyTour(Long tourId) {
    Tour tour = tourRepository.findById(tourId).orElseThrow();
    tour.setStatus(TourStatusEnum.DENY);
    return tourToTourDtoConverter.convert(tourRepository.save(tour));
  }

  @Override
  public List<TourDTO> getPendingTour() {
    return tourRepository.findAll().stream()
        .filter(tour -> TourStatusEnum.PENDING.equals(tour.getStatus()))
        .map(tourToTourDtoConverter::convert)
        .collect(Collectors.toList());
  }

  @Override
  public Tour findTourById(Long id) {
    return tourRepository.findById(id).orElse(null);
  }
}
