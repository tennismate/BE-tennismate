package com.example.tennismate.global.exception.custom;

import com.example.tennismate.global.exception.errorcode.ErrorCode;
import lombok.Getter;

@Getter
public class DuplicatedException extends RuntimeException {
    private final ErrorCode errorCode;
    public DuplicatedException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
