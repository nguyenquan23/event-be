package com.example.localguidebe.service;

import com.example.localguidebe.dto.coinDTO.coinpayments.response.Result.BaseResponse;
import com.example.localguidebe.dto.coinDTO.coinpayments.response.Result.BillInfoData;
import com.example.localguidebe.dto.coinDTO.coinpayments.response.Result.WithdrawalInfo;
import com.example.localguidebe.dto.coinDTO.coinpayments.response.info.DepositAddress;
import com.example.localguidebe.dto.coinDTO.coinpayments.resquest.TransactionRequest;

public interface CoinpaymentsService {
    BaseResponse<DepositAddress> getDepositAddress(String currency);

    Double getCoinAmount(Double usdAmount);

    WithdrawalInfo createWithdrawal(TransactionRequest transactionRequest);
    Double getUSDAmount(Double coinAmount) ;
     Double getUSDAndLTCTRate();

    BillInfoData getPaymentInfo(String txid);
}
