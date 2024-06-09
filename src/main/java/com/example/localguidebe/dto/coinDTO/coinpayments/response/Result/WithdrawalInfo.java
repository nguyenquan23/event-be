package com.example.localguidebe.dto.coinDTO.coinpayments.response.Result;

import com.example.localguidebe.dto.coinDTO.coinpayments.response.info.WithdrawalData;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WithdrawalInfo {
    private String error;
    private WithdrawalData result ;
}
