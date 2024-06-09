package com.example.localguidebe.dto.coinDTO.coinpayments.resquest;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PassengerInfo {
    private String fullName;
    private String phone;
    private String email;
}
