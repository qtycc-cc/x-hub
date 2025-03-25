package com.example.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

import com.example.model.exception.AccountHasBeenUsedException;
import com.example.model.exception.BusinessException;
import com.example.model.exception.InvalidCredentialsException;
import com.example.model.exception.MyIllegalArgumentException;
import com.example.model.exception.PermissionDeniedException;
import com.example.model.response.ExceptionResponse;
import com.example.model.response.R;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MyIllegalArgumentException.class)
    public ResponseEntity<R<ExceptionResponse>> handleMyIllegalArgumentException(MyIllegalArgumentException e) {
        log.error("MyIllegalArgumentException: the message is {}, and the cause is {}", e.getMessage(), e.getCause());
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setReason(e.getMessage());
        return ResponseEntity.badRequest()
                .body(R.fail(e.getMessage(), exceptionResponse));
    }

    @ExceptionHandler(AccountHasBeenUsedException.class)
    public ResponseEntity<R<ExceptionResponse>> handleAccountHasBeenUsedException(AccountHasBeenUsedException e) {
        log.error("AccountHasBeenUsedException: the message is {}, and the cause is {}", e.getMessage(), e.getCause());
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setReason(e.getMessage());
        return ResponseEntity.badRequest()
                .body(R.fail(e.getMessage(), exceptionResponse));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<R<ExceptionResponse>> handleInvalidCredentialsException(InvalidCredentialsException e) {
        log.error("InvalidCredentialsException: the message is {}, and the cause is {}", e.getMessage(), e.getCause());
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setReason(e.getMessage());
        return ResponseEntity.badRequest()
                .body(R.fail(e.getMessage(), exceptionResponse));
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity<R<ExceptionResponse>> handlePermissionDeniedException(PermissionDeniedException e) {
        log.error("PermissionDeniedException: the message is {}, and the cause is {}", e.getMessage(), e.getCause());
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setReason(e.getMessage());
        return ResponseEntity.status(403)
                .body(R.fail(e.getMessage(), exceptionResponse));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<R<ExceptionResponse>> handleBussinessException(BusinessException e) {
        log.error("BussinessException: the message is {}, and the cause is {}", e.getMessage(), e.getCause());
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setReason(e.getMessage());
        return ResponseEntity.internalServerError()
                .body(R.fail(e.getMessage(), exceptionResponse));
    }
}
