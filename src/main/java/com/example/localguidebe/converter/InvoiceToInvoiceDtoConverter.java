package com.example.localguidebe.converter;

import com.example.localguidebe.dto.InvoiceDTO;
import com.example.localguidebe.entity.Invoice;
import org.springframework.stereotype.Component;

@Component
public class InvoiceToInvoiceDtoConverter {
  private final BookingToBookingDtoConverter bookingToBookingDtoConverter;

  public InvoiceToInvoiceDtoConverter(BookingToBookingDtoConverter bookingToBookingDtoConverter) {
    this.bookingToBookingDtoConverter = bookingToBookingDtoConverter;
  }

  public InvoiceDTO convert(Invoice source) {
    return new InvoiceDTO(
        source.getId(),
        source.getPriceTotal(),
        source.getCreateAt(),
        source.getVndPrice(),
        source.getConversionRate(),
        source.getFullName(),
        source.getPhone(),
        source.getEmail(),
        source.getStatus(),
        source.getBookings().stream().map(bookingToBookingDtoConverter::convert).toList());
  }
}
