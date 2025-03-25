package com.example.model.exception;

public class MyCannotAcquireLockException extends BusinessException {
    public MyCannotAcquireLockException(String message) {
        super(message);
    }

    public MyCannotAcquireLockException(String message, Throwable cause) {
        super(message, cause);
    }
}
