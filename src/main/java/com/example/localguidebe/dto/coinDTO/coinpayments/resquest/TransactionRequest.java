package com.example.localguidebe.dto.coinDTO.coinpayments.resquest;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionRequest {
    private String bookingIds;
    private String buyerEmail;
    private double amount;
    private PassengerInfo passengerInfo;
}
