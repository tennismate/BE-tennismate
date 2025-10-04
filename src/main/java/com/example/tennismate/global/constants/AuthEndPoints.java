package com.example.tennismate.global.constants;

public final class AuthEndPoints {
    // private 생성자로 객체 생성 막음
    private AuthEndPoints() {
    }

    public static final String[] SWAGGER_ENDPOINTS = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**"
    };

    public static final String[] AUTH_PERMIT_ENDPOINTS = {
            "/api/v1/members/signup",
            "/api/v1/members/login",
            "/api/v1/members/refresh"
    };
}
