package com.example.model.exception;

public class AccountHasBeenUsedException extends BusinessException {
    public AccountHasBeenUsedException(String message) {
        super(message);
    }

    public AccountHasBeenUsedException(String message, Throwable cause) {
        super(message, cause);
    }
}
