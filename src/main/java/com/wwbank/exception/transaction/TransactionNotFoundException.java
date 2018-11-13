package com.wwbank.exception.transaction;

public class TransactionNotFoundException extends Exception {
    public TransactionNotFoundException() {
    }

    public TransactionNotFoundException(String message) {
        super(message);
    }
}
