package com.wwbank.exception.client;

public class ClientAlreadyExistException extends Exception {

    public ClientAlreadyExistException() {
    }

    public ClientAlreadyExistException(String message) {
        super(message);
    }
}
