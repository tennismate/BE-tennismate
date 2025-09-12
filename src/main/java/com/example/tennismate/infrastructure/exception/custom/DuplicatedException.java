package com.example.tennismate.infrastructure.exception.custom;

import com.example.tennismate.infrastructure.exception.errorcode.ErrorCode;
import lombok.Getter;

@Getter
public class DuplicatedException extends RuntimeException {
    private final ErrorCode errorCode;
    public DuplicatedException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
