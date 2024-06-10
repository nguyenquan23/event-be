package com.example.localguidebe.service;

import com.example.localguidebe.dto.LocationDTO;
import com.example.localguidebe.dto.TourDTO;
import com.example.localguidebe.dto.requestdto.TourRequestDTO;
import com.example.localguidebe.dto.requestdto.UpdateTourRequestDTO;
import com.example.localguidebe.dto.responsedto.ReviewResponseDTO;
import com.example.localguidebe.dto.responsedto.SearchSuggestionResponseDTO;
import com.example.localguidebe.dto.responsedto.SearchTourDTO;
import com.example.localguidebe.entity.Tour;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TourService {
    SearchTourDTO getListTour(Integer page, Integer limit);

    Tour saveTour(TourRequestDTO tourRequestDTO, String email);

    Tour updateTour(UpdateTourRequestDTO updateTourRequestDTO);

    TourDTO getTourById(Long id);

    SearchTourDTO getTours(
            Integer page,
            Integer limit,
            String sortBy,
            String order,
            String searchKey,
            Double minPrice,
            Double maxPrice,
            List<Long> categoryId);

    List<TourDTO> deleteTour(Long id);

    List<Tour> getToursOfGuide(Long guideId);

    SearchTourDTO getListTourForAdmin(Integer page, Integer limit);

    SearchSuggestionResponseDTO getTourAndTourLocations(String searchValue);

    List<String> getTourStartTimeAvailable(Long tourId, LocalDate localDate);

    boolean checkBookingByTraveler(Long tourId, String email);

    boolean checkExistingReviewsByTraveler(Long travelerId, Long tourId);

    void updateRatingForTour(Tour tour);

    List<String> getLocationName(List<LocationDTO> locationDTOS);

    List<ReviewResponseDTO> filterReviewForTour(List<Integer> ratings, Long tourId, String sortBy);

    TourDTO acceptTour(Long tourId);

    TourDTO denyTour(Long tourId);

    List<TourDTO> getPendingTour();

    Tour findTourById(Long id);
    LocalDateTime getStartDateByBookingId(Long bookingId);
}
