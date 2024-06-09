package com.example.localguidebe.dto.requestdto;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record UpdateBookingDTO(Long id, Integer numberTravelers, LocalDateTime startDate, LocalTime startTime) {}
