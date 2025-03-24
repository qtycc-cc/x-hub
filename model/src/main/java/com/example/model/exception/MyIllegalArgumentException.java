package com.example.model.exception;

public class MyIllegalArgumentException extends BusinessException {
    public MyIllegalArgumentException(String message) {
        super(message);
    }

    public MyIllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }
}

