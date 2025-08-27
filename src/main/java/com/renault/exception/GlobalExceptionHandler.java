package com.renault.exception;

import com.renault.utils.ErrorEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ErrorEntity> dataNotFoundHandler(DataNotFoundException exception) {
        ErrorEntity error = ErrorEntity.builder()
                .timeStamp(LocalDateTime.now())
                .message(exception.getMessage())
                .httpStatus(HttpStatus.NOT_FOUND.value())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorEntity> runtimeExceptionHandler(RuntimeException exception) {
        ErrorEntity error = ErrorEntity.builder()
                .timeStamp(LocalDateTime.now())
                .message(exception.getMessage())
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(error);
    }
}
