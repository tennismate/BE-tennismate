package com.example.tennismate.member.application.service.impl;

import com.example.tennismate.global.enums.MemberRole;
import com.example.tennismate.global.exception.custom.DuplicatedException;
import com.example.tennismate.global.exception.errorcode.ErrorCode;
import com.example.tennismate.member.application.port.MemberRepositoryPort;
import com.example.tennismate.member.application.service.MemberService;
import com.example.tennismate.member.dto.request.MemberRegisterRequest;
import com.example.tennismate.member.entity.Member;
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
        // 중복 이메일 검증
        validateDuplicateEmail(memberRegisterRequest.email());

        // 중복 닉네임 검증
        validateDuplicateNickname(memberRegisterRequest.nickname());

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(memberRegisterRequest.password());

        // member 객체 생성
        Member member = Member.of(memberRegisterRequest.email(), encodedPassword, memberRegisterRequest.nickname(), memberRegisterRequest.phoneNumber(), memberRegisterRequest.age(), null, null, MemberRole.ROLE_USER);

        // member save
        memberRepository.register(member);
    }

    /**
     * 회원의 이메일 아이디 중복 검증
     * @param email : 이메일 아이디
     */
    private void validateDuplicateEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new DuplicatedException(ErrorCode.DUPLICATED_EMAIL);
        }
    }

    /**
     * 회원의 닉네임 중복 검증
     * @param nickname : 닉네임
     */
    private void validateDuplicateNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new DuplicatedException(ErrorCode.DUPLICATED_NICKNAME);
        }
    }
}
