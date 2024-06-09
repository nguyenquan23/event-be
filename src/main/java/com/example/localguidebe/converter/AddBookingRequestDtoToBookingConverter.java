package com.example.localguidebe.converter;

import com.example.localguidebe.dto.requestdto.AddBookingRequestDTO;
import com.example.localguidebe.entity.Booking;
import com.example.localguidebe.repository.TourRepository;
import org.springframework.stereotype.Component;

@Component
public class AddBookingRequestDtoToBookingConverter {
  private final TourRepository tourRepository;

  public AddBookingRequestDtoToBookingConverter(TourRepository tourRepository) {
    this.tourRepository = tourRepository;
  }

  public Booking convert(AddBookingRequestDTO addBookingRequestDTO) {

    return Booking.builder()
        .numberTravelers(addBookingRequestDTO.numberTravelers())
        .startDate(addBookingRequestDTO.startDate())
        .status(addBookingRequestDTO.status())
        .price(addBookingRequestDTO.price())
        .tour(tourRepository.findById(addBookingRequestDTO.id()).orElseThrow())
        .build();
  }
}
