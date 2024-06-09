package com.example.localguidebe.service.impl;

import com.example.localguidebe.converter.TourToTourDtoConverter;
import com.example.localguidebe.entity.Tour;
import com.example.localguidebe.entity.TourDupe;
import com.example.localguidebe.exception.ConvertTourToTourDupeException;
import com.example.localguidebe.repository.TourDupeRepository;
import com.example.localguidebe.repository.TourRepository;
import com.example.localguidebe.service.TourDupeService;
import org.springframework.stereotype.Service;

@Service
public class TourDupeServiceImpl implements TourDupeService {
  private final TourToTourDtoConverter tourToTourDtoConverter;
  private final TourRepository tourRepository;
  private final TourDupeRepository tourDupeRepository;

  public TourDupeServiceImpl(
      TourToTourDtoConverter tourToTourDtoConverter,
      TourRepository tourRepository,
      TourDupeRepository tourDupeRepository) {
    this.tourToTourDtoConverter = tourToTourDtoConverter;
    this.tourRepository = tourRepository;
    this.tourDupeRepository = tourDupeRepository;
  }

  @Override
  public TourDupe addTourDupe(Tour tour) {
    TourDupe tourDupe = null;
    try {
      tourDupe = new TourDupe(tourToTourDtoConverter.convert(tour));
    } catch (Exception e) {
      throw new ConvertTourToTourDupeException(e.getMessage());
    }
    tour.setTourDupe(tourDupeRepository.save(tourDupe));
    tourRepository.save(tour);
    return tourDupe;
  }

  @Override
  public TourDupe getTourDupe(Tour tour) {
    return tour.getTourDupe() == null ? addTourDupe(tour) : tour.getTourDupe();
  }
}
