package com.example.localguidebe.dto.requestdto;

import com.example.localguidebe.dto.TourDTO;
import com.example.localguidebe.enums.BookingStatusEnum;

import java.time.LocalDateTime;

public record AddBookingRequestDTO(
    Long id,
    LocalDateTime startDate,
    Integer numberTravelers,
    Double price,
    BookingStatusEnum status,
    boolean isDeleted) {}
