package com.example.localguidebe.service.impl;

import com.example.localguidebe.crypto.client.CoinpaymentsClient;
import com.example.localguidebe.crypto.configuration.CoinpaymentsProperties;
import com.example.localguidebe.dto.coinDTO.coinpayments.response.Result.BaseResponse;
import com.example.localguidebe.dto.coinDTO.coinpayments.response.Result.BillInfoData;
import com.example.localguidebe.dto.coinDTO.coinpayments.response.Result.CoinInfo;
import com.example.localguidebe.dto.coinDTO.coinpayments.response.Result.WithdrawalInfo;
import com.example.localguidebe.dto.coinDTO.coinpayments.response.info.DepositAddress;
import com.example.localguidebe.dto.coinDTO.coinpayments.resquest.PaymentInfoBody;
import com.example.localguidebe.dto.coinDTO.coinpayments.resquest.RequestBody;
import com.example.localguidebe.dto.coinDTO.coinpayments.resquest.TransactionRequest;
import com.example.localguidebe.dto.coinDTO.coinpayments.resquest.WithdrawalBody;
import com.example.localguidebe.enums.Cmd;
import com.example.localguidebe.enums.Currency;
import com.example.localguidebe.service.CoinpaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoinpaymentsServiceImpl implements CoinpaymentsService {

    private final CoinpaymentsProperties coinpaymentsProperties;
    private final CoinpaymentsClient coinpaymentsClient;

    @Autowired
    public CoinpaymentsServiceImpl(CoinpaymentsProperties coinpaymentsProperties,
                                   CoinpaymentsClient coinpaymentsClient) {
        this.coinpaymentsProperties = coinpaymentsProperties;
        this.coinpaymentsClient = coinpaymentsClient;
    }

    @Override
    public BaseResponse<DepositAddress> getDepositAddress(String currency) {
        final String requestBody = new RequestBody(coinpaymentsProperties.getVersion(), Cmd.RATES.getCmd(),
                currency, coinpaymentsProperties.getPublicKey()).toString();
        CoinInfo coinInfo = coinpaymentsClient.getRates(requestBody);
        return coinpaymentsClient.getDepositAddres(requestBody);
    }

    @Override
    public Double getCoinAmount(Double usdAmount) {
        final String requestBody = new RequestBody(coinpaymentsProperties.getVersion(), Cmd.RATES.getCmd(),
                Currency.ltct.toString(), coinpaymentsProperties.getPublicKey()).toString();
        CoinInfo coinInfo = coinpaymentsClient.getRates(requestBody);
        return coinInfo.getResult().get("USD").getRate_btc() / coinInfo.getResult().get("LTC").getRate_btc() * usdAmount;
    }
    @Override
    public Double getUSDAmount(Double coinAmount) {
        final String requestBody = new RequestBody(coinpaymentsProperties.getVersion(), Cmd.RATES.getCmd(),
                Currency.ltct.toString(), coinpaymentsProperties.getPublicKey()).toString();
        CoinInfo coinInfo = coinpaymentsClient.getRates(requestBody);
        return   coinInfo.getResult().get("LTC").getRate_btc()/coinInfo.getResult().get("USD").getRate_btc() * coinAmount;
    }
    @Override
    public Double getUSDAndLTCTRate(){
        final String requestBody = new RequestBody(coinpaymentsProperties.getVersion(), Cmd.RATES.getCmd(),
                Currency.ltct.toString(), coinpaymentsProperties.getPublicKey()).toString();
        CoinInfo coinInfo = coinpaymentsClient.getRates(requestBody);
        return   coinInfo.getResult().get("LTC").getRate_btc()/coinInfo.getResult().get("USD").getRate_btc();
    }



    @Override
    public WithdrawalInfo createWithdrawal(TransactionRequest transactionRequest) {
        final String request = new WithdrawalBody(coinpaymentsProperties.getVersion(), coinpaymentsProperties.getPublicKey().toString(), Cmd.CREATE_WITHDRAWAL.getCmd(), transactionRequest.getAmount(), Currency.ltct.toString(), "mimZqNG3fBVP2jyg47nKuTitdk7P6hZZwv", "1").toString();
        WithdrawalInfo withdrawalInfo = coinpaymentsClient.createWithdrawal(request);
        return withdrawalInfo;
    }

    @Override
    public BillInfoData getPaymentInfo(String txid) {
        final String request = new PaymentInfoBody(coinpaymentsProperties.getVersion(), coinpaymentsProperties.getPublicKey(), Cmd.GET_WITHDRAWAL_INFO.getCmd(), txid).toString();
        BillInfoData billInfoData = coinpaymentsClient.getInfoTransaction(request);
        return billInfoData;
    }

}
