package com.wwbank.exception.client;

public class ClientNotFoundException extends Exception {

    public ClientNotFoundException() {
    }

    public ClientNotFoundException(String message) {
        super(message);
    }
}
