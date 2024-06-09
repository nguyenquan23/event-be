package com.example.localguidebe.dto.coinDTO.coinpayments.response.Result;

import com.example.localguidebe.dto.coinDTO.coinpayments.response.info.Bitcoin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CoinResult {
    private Bitcoin BTC;
}
