package com.example.tennismate.member.controller;

import com.example.tennismate.global.exception.custom.DuplicatedException;
import com.example.tennismate.global.exception.errorcode.ErrorCode;
import com.example.tennismate.member.application.service.MemberService;
import com.example.tennismate.member.dto.request.MemberRegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

    // TODO : 중복 닉네임 예외처리 검증

    // TODO : @Valid 관련 예외처리 올바르게 검증되는지 테스트

}
