package com.example.ticsys.app.outbound.event;

public enum LinkBankAccountResult {
    SUCCESS,
    BANK_ACCOUNT_NOT_FOUND,
    WRONG_BANK_ACCOUNT_OWNER_NAME,
    LOCKED_BANK_ACCOUNT,
    UNKNOWN_ERROR,
    BANK_ACCOUNT_IS_ALREADY_LINKED
}
