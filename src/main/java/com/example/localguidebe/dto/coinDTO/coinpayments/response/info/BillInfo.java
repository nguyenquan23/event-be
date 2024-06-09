package com.example.localguidebe.dto.coinDTO.coinpayments.response.info;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BillInfo {
    private long timeCreated;
    private int status;
    private String statusText;
    private String coin;
    private long amount;
    private String amountf;
    private String sendAddress;
    private String sendTxid;
}
