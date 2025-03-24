package com.example.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

import com.example.model.exception.BusinessException;
import com.example.model.response.ExceptionResponse;
import com.example.model.response.R;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<R<ExceptionResponse>> handleBussinessException(BusinessException e) {
        log.error("BussinessException: the message is {}, and the cause is {}", e.getMessage(), e.getCause());
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setReason(e.getMessage());
        return ResponseEntity.internalServerError()
                .body(R.fail(e.getMessage(), exceptionResponse));
    }
}
