package com.example.tennismate.global.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(staticName = "of")
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;

    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.of(200, "요청이 성공했습니다.", data);
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        return ApiResponse.of(200, message, data);
    }

    public static <T> ApiResponse<T> error(int status, String message) {
        return ApiResponse.of(status, message, null);
    }
}
