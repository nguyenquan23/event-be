package com.example.localguidebe.dto.coinDTO.coinpayments.response.Result;

import com.example.localguidebe.dto.coinDTO.coinpayments.response.info.BillInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BillInfoData {
    private String error;
    private BillInfo result ;
}
