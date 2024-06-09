package com.example.localguidebe.dto.coinDTO.coinpayments.response.info;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class DepositAddress {
    private String address;


    @JsonProperty("pubkey")
    private String publicKey;

    @JsonProperty("dest_tag")
    private String destinationTag;
}
