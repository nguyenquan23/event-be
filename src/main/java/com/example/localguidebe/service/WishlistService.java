package com.example.localguidebe.service;

import com.example.localguidebe.dto.TourDTO;
import com.example.localguidebe.dto.UserDTO;
import java.util.List;

public interface WishlistService {
  UserDTO addWishlist(Long tourId, Long travelerId);

  List<TourDTO> getWishlist(Long travelerId);

  List<TourDTO> deleteTourInWishlist(Long tourId, Long travelerId);
}
