package com.example.localguidebe.dto.coinDTO.coinpayments.response.Result;

import com.example.localguidebe.dto.coinDTO.coinpayments.response.info.Bitcoin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CoinInfo {
    private String error;
    private Map<String, Bitcoin> result = new LinkedHashMap<>();
}
