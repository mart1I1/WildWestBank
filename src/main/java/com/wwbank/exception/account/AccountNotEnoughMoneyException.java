package com.wwbank.exception.account;

public class AccountNotEnoughMoneyException extends Exception {
    public AccountNotEnoughMoneyException() {
    }

    public AccountNotEnoughMoneyException(String message) {
        super(message);
    }
}
