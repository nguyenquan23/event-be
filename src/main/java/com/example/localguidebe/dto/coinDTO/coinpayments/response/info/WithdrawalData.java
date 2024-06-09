package com.example.localguidebe.dto.coinDTO.coinpayments.response.info;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WithdrawalData {
    private String id;
    private Integer status;
    private double amount;
}
