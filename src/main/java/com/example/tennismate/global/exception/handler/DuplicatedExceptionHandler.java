package com.example.tennismate.global.exception.handler;

import com.example.tennismate.global.exception.custom.DuplicatedException;
import com.example.tennismate.global.exception.errorcode.ErrorCode;
import com.example.tennismate.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DuplicatedExceptionHandler {
    @ExceptionHandler(value = DuplicatedException.class)
    public ResponseEntity<ApiResponse<?>> duplicatedExceptionHandler(DuplicatedException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.error(errorCode.getStatus().value(), errorCode.getMessage()));
    }
}
