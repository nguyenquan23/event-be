package com.example.localguidebe.dto.coinDTO.coinpayments.response.info;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Bitcoin {
    private int is_fiat;
    private Double rate_btc;
    private String last_update;
    private String tx_fee;
    private String status;
    private String image;
    private String name;
    private String confirms;
    private List<String> capabilities;
}
