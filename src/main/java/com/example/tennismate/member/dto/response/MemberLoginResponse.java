package com.example.tennismate.member.dto.response;

public record MemberLoginResponse(
        String email,
        String nickname,
        String role,
        String accessToken,
        String refreshToken
) {
    public static MemberLoginResponse of(String email, String nickname, String role, String accessToken, String refreshToken) {
        return new MemberLoginResponse(email, nickname, role, accessToken, refreshToken);
    }
}
