package com.example.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

import com.example.model.exception.BusinessException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public String handleBussinessException(BusinessException e) {
        log.error("BussinessException: the message is {}, and the cause is {}", e.getMessage(), e.getCause());
        return e.getMessage();
    }
}
