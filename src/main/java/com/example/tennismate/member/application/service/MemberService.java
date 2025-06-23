package com.example.tennismate.member.application.service;

import com.example.tennismate.member.dto.request.MemberRegisterRequest;

public interface MemberService {
    void register(MemberRegisterRequest memberRegisterRequest);
}
