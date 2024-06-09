package com.example.localguidebe.dto;

import java.util.List;

public record CartDTO(Long id, List<BookingDTO> bookings) {}
