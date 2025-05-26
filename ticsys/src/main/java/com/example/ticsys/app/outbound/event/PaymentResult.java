package com.example.ticsys.app.outbound.event;

public enum PaymentResult {
    SUCCESS,
    LOCKED_ACCOUNT,
    INSUFFICIENT_BALANCE,
    INVALID_AMOUNT,
    SOURCE_NOT_FOUND,
    DESTINATION_NOT_FOUND,
    ACCOUNT_NOT_FOUND,
    UNKNOWN_ERROR
}
