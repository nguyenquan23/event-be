package com.example.localguidebe.dto.coinDTO.coinpayments.response.info;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class DepositAddressDTO {
    private String address;
}
