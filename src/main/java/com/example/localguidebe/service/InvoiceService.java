package com.example.localguidebe.service;

import com.example.localguidebe.entity.Invoice;
import com.example.localguidebe.enums.InvoiceStatus;
import java.util.List;
import java.util.Optional;

public interface InvoiceService {
  Invoice createBookingInInvoice(
      String vnp_TxnRef,
      List<Long> bookingIds,
      String email,
      String travelerEmail,
      String fullName,
      String phone,
      Double priceTotal,
      Double priceInVND,
      Double usdVndRate);

  Invoice refundInvoice(Invoice invoice, Double refundVndPrice);

  Optional<Invoice> findById(Long id);

  Double getRefundAmount(Invoice invoice);
}
