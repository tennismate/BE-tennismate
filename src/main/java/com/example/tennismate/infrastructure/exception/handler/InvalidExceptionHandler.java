package com.example.tennismate.infrastructure.exception.handler;

import com.example.tennismate.global.response.ApiResponse;
import com.example.tennismate.infrastructure.exception.custom.InvalidTokenException;
import com.example.tennismate.infrastructure.exception.custom.NotFoundException;
import com.example.tennismate.infrastructure.exception.errorcode.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InvalidExceptionHandler {
    @ExceptionHandler(value = InvalidTokenException.class)
    public ResponseEntity<ApiResponse<?>> invalidTokenExceptionHandler(InvalidTokenException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.error(errorCode.getStatus().value(), errorCode.getMessage()));
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ApiResponse<?>> notFoundExceptionHandler(NotFoundException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.error(errorCode.getStatus().value(), errorCode.getMessage()));
    }
}
