package com.example.localguidebe.converter;

import com.example.localguidebe.dto.BookingDTO;
import com.example.localguidebe.entity.Booking;
import com.example.localguidebe.exception.ConvertTourToTourDupeException;
import org.springframework.stereotype.Component;

@Component
public class BookingToBookingDtoConverter {
  private final TourToTourInBookingResponseDtoConverter tourToTourInBookingResponseDtoConverter;
  private final UserToGuideDtoConverter userToGuideDtoConverter;
  private final TourDupeToTourDtoConverter tourDupeToTourDtoConverter;

  public BookingToBookingDtoConverter(
      TourDupeToTourDtoConverter tourDupeToTourDtoConverter,
      TourToTourInBookingResponseDtoConverter tourToTourInBookingResponseDtoConverter,
      UserToGuideDtoConverter userToGuideDtoConverter) {
    this.tourDupeToTourDtoConverter = tourDupeToTourDtoConverter;
    this.tourToTourInBookingResponseDtoConverter = tourToTourInBookingResponseDtoConverter;
    this.userToGuideDtoConverter = userToGuideDtoConverter;
  }

  public BookingDTO convert(Booking source) {
    try {
      return new BookingDTO(
          source.getId(),
          source.getStartDate(),
          source.getNumberTravelers(),
          source.getPrice(),
          source.getStatus(),
          tourToTourInBookingResponseDtoConverter.convert(source.getTour()),
          source.getTourDupe() != null
              ? tourDupeToTourDtoConverter.convert(source.getTourDupe())
              : null,
          userToGuideDtoConverter.convert(source.getTour().getGuide()));
    } catch (Exception e) {
      throw new ConvertTourToTourDupeException(e.getMessage());
    }
  }
}
