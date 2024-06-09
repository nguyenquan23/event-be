package com.example.localguidebe.dto.coinDTO.coinpayments.resquest;

import lombok.*;

@Data
@AllArgsConstructor
public class WithdrawalBody {
    private String version;
    private String key;
    private String cmd;
    private double amount;
    private String currency;
    private String address;
    private String auto_confirm;
    public String toString() {
        return String.format("version=%s&key=%s&cmd=%s&amount=%s&currency=%s&address=%s&auto_confirm=%s",
                this.version,
                this.key,
                this.cmd,
                this.amount,
                this.currency,
                this.address,
                this.auto_confirm);
    }

}
