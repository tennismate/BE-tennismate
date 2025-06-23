package com.example.tennismate.member.application.service;

import com.example.tennismate.member.application.port.MemberRepositoryPort;
import com.example.tennismate.member.dto.request.MemberRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepositoryPort memberRepository;

    @Transactional
    public void register(MemberRegisterRequest memberRegisterRequest) {

    }
}
