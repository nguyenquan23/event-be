package com.example.localguidebe.dto.coinDTO.coinpayments.resquest;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentInfoBody {
    private String version;
    private String key;
    private String cmd;
    private String id;

    @Override
    public String toString() {
        return String.format("version=%s&key=%s&cmd=%s&id=%s",
                this.version,
                this.key,
                this.cmd,
                this.id);
    }
}
