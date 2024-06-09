package com.example.localguidebe.enums;

import lombok.Getter;

@Getter
public enum Cmd {
    /**
     * Extend this for other Cmd calls
     */
    DEPOSIT_ADDRESS("get_deposit_address"),
    GET_CALLBACK_ADDRESS("get_callback_address"),
    RATES("rates "),
    CREATE_WITHDRAWAL("create_withdrawal"),
    GET_WITHDRAWAL_INFO("get_withdrawal_info");


    /* Name used on the enumeration */
    public final String cmd;

    Cmd(String cmd) {
        this.cmd = cmd;
    }
}
