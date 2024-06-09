package com.example.localguidebe.service.impl;

import com.example.localguidebe.converter.TourToTourDtoConverter;
import com.example.localguidebe.converter.UserToUserDtoConverter;
import com.example.localguidebe.dto.TourDTO;
import com.example.localguidebe.dto.UserDTO;
import com.example.localguidebe.entity.Tour;
import com.example.localguidebe.entity.User;
import com.example.localguidebe.repository.TourRepository;
import com.example.localguidebe.repository.UserRepository;
import com.example.localguidebe.service.WishlistService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class WishlistServiceImpl implements WishlistService {
  private final TourRepository tourRepository;
  private final UserRepository userRepository;
  private final UserToUserDtoConverter userToUserDtoConverter;
  private final TourToTourDtoConverter tourToTourDtoConverter;

  public WishlistServiceImpl(
      UserRepository userRepository,
      TourRepository tourRepository,
      UserToUserDtoConverter userToUserDtoConverter,
      TourToTourDtoConverter tourToTourDtoConverter) {
    this.tourRepository = tourRepository;
    this.userRepository = userRepository;
    this.userToUserDtoConverter = userToUserDtoConverter;
    this.tourToTourDtoConverter = tourToTourDtoConverter;
  }

  @Override
  public UserDTO addWishlist(Long tourId, Long travelerId) {
    User traveler = userRepository.findById(travelerId).orElseThrow();
    Tour tour = tourRepository.findById(tourId).orElseThrow();
    traveler.getWishListTour().add(tour);
    tour.getUsers().add(traveler);
    traveler = userRepository.save(traveler);
    return userToUserDtoConverter.convert(traveler);
  }

  @Override
  public List<TourDTO> getWishlist(Long travelerId) {
    User user = userRepository.findById(travelerId).orElseThrow();
    user.getWishListTour();
    return user.getWishListTour().stream().map(tourToTourDtoConverter::convert).toList();
  }

  @Override
  public List<TourDTO> deleteTourInWishlist(Long tourId, Long travelerId) {
    User traveler = userRepository.findById(travelerId).orElseThrow();
    Tour tour = tourRepository.findById(tourId).orElseThrow();
    traveler.getWishListTour().remove(tour);
    tour.getUsers().remove(traveler);
    return userRepository.save(traveler).getWishListTour().stream()
        .map(tourToTourDtoConverter::convert)
        .toList();
  }
}
