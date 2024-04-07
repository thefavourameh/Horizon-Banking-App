package com.favour.Horizon.Banking.App.infrastrusture.exception;

public class EmailNotSentException extends RuntimeException{
    public EmailNotSentException(String message) {
        super(message);
    }
}
