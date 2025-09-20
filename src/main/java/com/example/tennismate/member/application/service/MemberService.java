package com.example.tennismate.member.application.service;

import com.example.tennismate.member.dto.request.MemberRegisterRequest;
import com.example.tennismate.member.dto.response.TokenResponse;

public interface MemberService {
    void register(MemberRegisterRequest memberRegisterRequest);

    TokenResponse reissueToken(String refreshToken);
}
