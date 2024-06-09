package com.example.localguidebe.dto.coinDTO;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApiError {
    private ErrorType errorType;
    private String message;

    @Getter
    public enum ErrorType {
        INVALID_CURRENCY("Invalid currency");

        private final String errorName;

        ErrorType(String errorName) {
            this.errorName = errorName;
        }
    }
}
