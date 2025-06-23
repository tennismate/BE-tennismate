package com.example.tennismate.member.application.service.impl;

import com.example.tennismate.global.exception.custom.DuplicatedException;
import com.example.tennismate.global.exception.errorcode.ErrorCode;
import com.example.tennismate.member.application.port.MemberRepositoryPort;
import com.example.tennismate.member.application.service.MemberService;
import com.example.tennismate.member.dto.request.MemberRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepositoryPort memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void register(MemberRegisterRequest memberRegisterRequest) {
        // TODO : 중복 이메일 검증
        validateDuplicateEmail(memberRegisterRequest.email());

        // TODO : 중복 닉네임 검증

        // TODO : 비밀번호 암호화

        // TODO : member 객체 생성

        // TODO : member save
    }

    private void validateDuplicateEmail(String email) {
        if (!memberRepository.existsByEmail(email)) {
            throw new DuplicatedException(ErrorCode.DUPLICATED_EMAIL);
        }
    }
}
