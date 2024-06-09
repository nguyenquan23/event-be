package com.example.localguidebe.controller;

import com.example.localguidebe.dto.coinDTO.coinpayments.response.Result.WithdrawalInfo;
import com.example.localguidebe.dto.coinDTO.coinpayments.resquest.TransactionRequest;
import com.example.localguidebe.entity.Invoice;
import com.example.localguidebe.security.service.CustomUserDetails;
import com.example.localguidebe.service.CoinpaymentsService;
import com.example.localguidebe.service.InvoiceService;
import com.example.localguidebe.system.Result;
import com.example.localguidebe.utils.AuthUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    private final CoinpaymentsService coinpaymentsService;
    private final InvoiceService invoiceService;


    public PaymentController(CoinpaymentsService coinpaymentsService, InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
        this.coinpaymentsService = coinpaymentsService;
    }

    @GetMapping("/amount/{amount}")
    public ResponseEntity<Result> getCoinAmount(@PathVariable("amount") Double amount) {
        try {
            return new ResponseEntity<>(
                    new Result(
                            false,
                            HttpStatus.OK.value(),
                            "Successfully get mount of coin",
                            coinpaymentsService.getCoinAmount(amount)),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new Result(
                            false,
                            HttpStatus.CONFLICT.value(),
                            "Failed to get mount of coin",
                            null),
                    HttpStatus.CONFLICT);
        }

    }

    @PostMapping("/transaction")
    @Transactional
    public ResponseEntity<Result> getTransaction(@RequestBody TransactionRequest transactionRequest, Authentication authentication) {
    return AuthUtils.checkAuthentication(
        authentication,
        () -> {
          try {
            int statusCheck = 0;
            WithdrawalInfo withdrawalInfo =
                coinpaymentsService.createWithdrawal(transactionRequest);

            while (statusCheck != 2) {
              System.out.println(statusCheck);
                statusCheck =   coinpaymentsService
                  .getPaymentInfo(withdrawalInfo.getResult().getId())
                  .getResult()
                  .getStatus();
              if (statusCheck == -1) {
                return new ResponseEntity<>(
                    new Result(false, HttpStatus.CONFLICT.value(), "Payment failed", null),
                    HttpStatus.CONFLICT);
              }
              Thread.sleep(10000);
            }

            List<Long> bookingIds =
                Arrays.stream(transactionRequest.getBookingIds().split(","))
                    .map(Long::valueOf)
                    .toList();
            Invoice invoice =
                invoiceService.createBookingInInvoice(
                    withdrawalInfo.getResult().getId(),
                    bookingIds,
                    transactionRequest.getPassengerInfo().getEmail(),
                    ((CustomUserDetails) authentication.getPrincipal()).getEmail(),
                    transactionRequest.getPassengerInfo().getFullName(),
                    transactionRequest.getPassengerInfo().getPhone(),
                    coinpaymentsService.getUSDAmount(transactionRequest.getAmount()),
                    transactionRequest.getAmount(),
                    coinpaymentsService.getUSDAndLTCTRate());
            return new ResponseEntity<>(
                new Result(false, HttpStatus.OK.value(), "Payment success", invoice.getId()),
                HttpStatus.OK);
          } catch (Exception e) {

            return new ResponseEntity<>(
                new Result(false, HttpStatus.CONFLICT.value(), "Payment failed", null),
                HttpStatus.CONFLICT);
          }
        });
    }

}
