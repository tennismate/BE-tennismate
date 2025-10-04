package com.example.tennismate.member.application.service.impl;

import com.example.tennismate.infrastructure.exception.custom.DuplicatedException;
import com.example.tennismate.infrastructure.exception.custom.InvalidTokenException;
import com.example.tennismate.infrastructure.exception.custom.NotFoundException;
import com.example.tennismate.infrastructure.exception.errorcode.ErrorCode;
import com.example.tennismate.infrastructure.security.jwt.JwtProvider;
import com.example.tennismate.member.application.port.MemberRepositoryPort;
import com.example.tennismate.member.application.service.MemberService;
import com.example.tennismate.member.dto.request.MemberRegisterRequest;
import com.example.tennismate.member.dto.response.TokenResponse;
import com.example.tennismate.member.entity.Member;
import com.example.tennismate.member.enums.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepositoryPort memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

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
        Member member = Member.of(memberRegisterRequest.email(), encodedPassword, memberRegisterRequest.nickname(), memberRegisterRequest.phoneNumber(), memberRegisterRequest.age(), null, null, null, MemberRole.ROLE_USER);

        // member save
        memberRepository.register(member);
    }

    @Override
    public TokenResponse reissueToken(String refreshToken) {
        // 1. Refresh Token 유효성 검증
        if (jwtProvider.validateToken(refreshToken)) {
            throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
        }

        // 2. Refresh Token 에서 이메일 추출
        String email = jwtProvider.getUserInfoFromToken(refreshToken).get("email", String.class);

        // 3. DB 에 저장된 Refresh Token 과 일치하는지 확인
        Member member = memberRepository.findMemberByEmail(email).orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        if (!member.getRefreshToken().equals(refreshToken)) {
            throw new InvalidTokenException(ErrorCode.MISMATCHED_REFRESH_TOKEN);
        }

        // TODO : 4. 새로운 Access Token 생성

        // TODO : 5. 새로운 Refresh Token 생성 후 DB 저장

        return null;
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
