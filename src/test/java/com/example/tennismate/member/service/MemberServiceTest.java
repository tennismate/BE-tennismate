package com.example.tennismate.member.service;

import com.example.tennismate.global.exception.custom.DuplicatedException;
import com.example.tennismate.global.exception.errorcode.ErrorCode;
import com.example.tennismate.member.application.port.MemberRepositoryPort;
import com.example.tennismate.member.application.service.impl.MemberServiceImpl;
import com.example.tennismate.member.dto.request.MemberRegisterRequest;
import com.example.tennismate.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName(value = "회원 서비스 테스트")
public class MemberServiceTest {
    @InjectMocks
    private MemberServiceImpl memberService;

    @Mock
    private MemberRepositoryPort memberRepositoryPort;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName(value = "회원가입 성공 테스트")
    void givenMemberRegisterRequest_whenRegisterMember_thenSuccess() {
        // given
        MemberRegisterRequest request = MemberRegisterRequest.of(
                "test@email.com",
                "testPassword123!",
                "testNickname",
                "010-1111-2222",
                25
        );

        // when
        when(memberRepositoryPort.existsByEmail(request.email())).thenReturn(false);
        when(memberRepositoryPort.existsByNickname(request.nickname())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");

        memberService.register(request);

        // then
        verify(memberRepositoryPort, times(1)).register(any(Member.class));

    }

    @Test
    @DisplayName(value = "회원가입 실패 - 중복된 이메일")
    void givenMemberRegisterRequest_whenEmailDuplicated_thenThrowsException() {
        // given
        MemberRegisterRequest request = MemberRegisterRequest.of(
                "duplicated@email.com",
                "testPassword123!",
                "testNickname",
                "010-1111-2222",
                25
        );

        // when
        when(memberRepositoryPort.existsByEmail(request.email())).thenReturn(true);

        // then
        DuplicatedException exception = assertThrows(DuplicatedException.class, () -> {
            memberService.register(request);
        });
        assertEquals(ErrorCode.DUPLICATED_EMAIL, exception.getErrorCode());
        verify(memberRepositoryPort, never()).register(any(Member.class));

    }

    @Test
    @DisplayName(value = "회원가입 실패 - 중복된 닉네임")
    void givenMemberRegisterRequest_whenDuplicatedNickname_thenThrowsException() {
        // given
        MemberRegisterRequest request = MemberRegisterRequest.of(
                "test@email.com",
                "testPassword123!",
                "duplicatedNickname",
                "010-1111-2222",
                25
        );

        // when
        when(memberRepositoryPort.existsByNickname(request.nickname())).thenReturn(true);

        // then
        DuplicatedException exception = assertThrows(DuplicatedException.class, () -> {
            memberService.register(request);
        });
        assertEquals(ErrorCode.DUPLICATED_NICKNAME, exception.getErrorCode());
        verify(memberRepositoryPort, never()).register(any(Member.class));

    }

    @Test
    @DisplayName(value = "비밀번호 암호화 테스트")
    void givenMemberRegisterRequestAndRawPassword_whenEncodingPassword_thenEqualPassword() {
        // given
        String rawPassword = "rawPassword123!@";
        String encodedPassword = "encodedPassword";
        MemberRegisterRequest request = MemberRegisterRequest.of(
                "test@email.com",
                rawPassword,
                "testNickname",
                "010-1111-2222",
                25
        );

        // when
        when(memberRepositoryPort.existsByEmail(request.email())).thenReturn(false);
        when(memberRepositoryPort.existsByNickname(request.nickname())).thenReturn(false);
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        memberService.register(request);

        // then
        verify(passwordEncoder, times(1)).encode(rawPassword);
        verify(memberRepositoryPort).register(argThat(member ->
                member.getPassword().equals(encodedPassword)
        ));
    }
}
