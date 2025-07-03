package com.example.tennismate.member.service;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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
}
