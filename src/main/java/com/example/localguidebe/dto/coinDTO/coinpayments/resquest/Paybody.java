package com.example.localguidebe.dto.coinDTO.coinpayments.resquest;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Paybody {
    private String version;
    private double amount;
    private String cmd;
    private String currency1;
    private String currency2;
    private String buyer_email;
    private String key;

    @Override
    public String toString() {
        return String.format("version=%s&amount=%s&cmd=%s&currency1=%s&currency2=%s&buyer_email=%s&key=%s",
                this.version,
                this.amount,
                this.cmd,
                this.currency1,
                this.currency2,
                this.buyer_email,
                this.key);
    }
}
