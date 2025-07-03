package com.example.tennismate.member.controller;

import com.example.tennismate.global.exception.custom.DuplicatedException;
import com.example.tennismate.global.exception.errorcode.ErrorCode;
import com.example.tennismate.member.application.service.MemberService;
import com.example.tennismate.member.dto.request.MemberRegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @Test
    @DisplayName(value = "회원가입 성공")
    void memberRegisterSuccess() throws Exception {
        // given
        MemberRegisterRequest request = MemberRegisterRequest.of(
                "test@email.com",
                "testpw1234!!",
                "testNickname",
                "010-1111-2222",
                25
        );

        // when
        doNothing().when(memberService).register(request);

        // then
        mockMvc.perform(post("/api/v1/members/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("요청이 성공했습니다."))
                .andExpect(jsonPath("$.data").value("회원가입이 완료되었습니다."));
    }

    @Test
    @DisplayName(value = "중복 이메일 테스트")
    void givenMemberRequest_whenDuplicatedException_thenCheckAPIDataMessage() throws Exception {
        // given
        MemberRegisterRequest request = MemberRegisterRequest.of(
                "duplicated@email.com",
                "testPw1234!!",
                "testNickName",
                "010-1111-2222",
                25
        );

        // when
        doThrow(new DuplicatedException(ErrorCode.DUPLICATED_EMAIL)).when(memberService).register(any(MemberRegisterRequest.class));

        // then
        mockMvc.perform(post("/api/v1/members/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                ).andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("이미 존재하는 이메일입니다."));
    }

    @Test
    @DisplayName(value = "중복 닉네임 테스트")
    void givenMemberRequest_whenRegisterMember_thenThrowDuplicatedNicknameException() throws Exception {
        // given
        MemberRegisterRequest request = MemberRegisterRequest.of(
                "test@email.com",
                "testPw1234!!",
                "testDuplicated",
                "010-1111-2222",
                25
        );

        // when
        doThrow(new DuplicatedException(ErrorCode.DUPLICATED_NICKNAME)).when(memberService).register(any(MemberRegisterRequest.class));

        // then
        mockMvc.perform(post("/api/v1/members/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                ).andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("이미 존재하는 닉네임 입니다."));

    }

    @DisplayName(value = "회원가입 요청 유효성 검증 실패 테스트")
    @ParameterizedTest(name = "[{index}] {4}")
    @CsvSource(
            value = {
                    // email, password, nickname, phoneNumber, expectedMessage
                    "'null', 'ValidPass123!', 'nickname', '010-1111-2222', '이메일은 필수 입력사항입니다.', 'email'",
                    "'invalid-email', 'ValidPass123!', 'nickname', '010-1111-2222', '이메일 형식을 지켜 입력해주세요.', 'email'",
                    "'test@email.com', 'null', 'nickname', '010-1111-2222', '비밀번호는 필수 입력사항입니다.', 'password'",
                    "'test@email.com', 'invalid-password', 'nickname', '010-1111-2222', '비밀번호는 영문, 숫자, 특수문자를 모두 포함해야합니다.', 'password'",
                    "'test@email.com', 'ValidPass123!', 'null', '010-1111-2222', '닉네임은 필수 입력사항입니다.', 'nickname'",
                    "'test@email.com', 'ValidPass123!', 'invalid nickname', '010-1111-2222', '닉네임은 공백 없이 영문, 한글, 숫자를 조합해서 입력해주세요.', 'nickname'",
                    "'test@email.com', 'ValidPass123!', 'nickname', 'null', '휴대폰 번호는 필수 입력사항입니다.', 'phoneNumber'",
                    "'test@email.com', 'ValidPass123!', 'nickname', '01012453432', '휴대폰 번호는 010-XXXX-XXXX 형식으로 입력해주세요.', 'phoneNumber'",
            },
            nullValues = "null"     // 소스코드의 "null" 문자열을 실제 null 값으로 변환
    )
    void givenMemberRegisterRequest_whenRegisterMember_thenCheckValidException(String email, String password, String nickname, String phoneNumber, String expectedMessage, String errorField) throws Exception {
        // given
        MemberRegisterRequest request = MemberRegisterRequest.of(
                email,
                password,
                nickname,
                phoneNumber,
                25
        );

        // when
        doNothing().when(memberService).register(any(MemberRegisterRequest.class));

        // then
        mockMvc.perform(post("/api/v1/members/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data." + errorField).value(expectedMessage));

    }

}
