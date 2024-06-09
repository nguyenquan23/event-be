package com.example.localguidebe.crypto.client;

import com.example.localguidebe.dto.coinDTO.coinpayments.response.Result.BaseResponse;
import com.example.localguidebe.dto.coinDTO.coinpayments.response.Result.BillInfoData;
import com.example.localguidebe.dto.coinDTO.coinpayments.response.Result.CoinInfo;
import com.example.localguidebe.dto.coinDTO.coinpayments.response.Result.WithdrawalInfo;
import com.example.localguidebe.dto.coinDTO.coinpayments.response.info.DepositAddress;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "coinpayments", url = "${coinpayments.url}")

public interface CoinpaymentsClient {

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    BaseResponse<DepositAddress> getDepositAddres(@RequestBody String requestBody);

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    CoinInfo getRates(@RequestBody String requestBody);

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    BillInfoData getInfoTransaction(@RequestBody String requestBody);

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    WithdrawalInfo createWithdrawal(@RequestBody String requestBody);

}

