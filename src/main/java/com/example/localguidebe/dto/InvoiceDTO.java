package com.example.localguidebe.dto;

import com.example.localguidebe.enums.InvoiceStatus;
import java.time.LocalDateTime;
import java.util.List;

public record InvoiceDTO(
    Long id,
    Double priceTotal,
    LocalDateTime createAt,
    Double vndPrice,
    Double conversionRate,
    String fullName,
    String phone,
    String email,
    InvoiceStatus status,
    List<BookingDTO> tours) {}
