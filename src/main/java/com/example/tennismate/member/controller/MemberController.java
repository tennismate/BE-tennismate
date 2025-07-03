package com.example.tennismate.member.controller;

import com.example.tennismate.global.response.ApiResponse;
import com.example.tennismate.member.application.service.MemberService;
import com.example.tennismate.member.dto.request.MemberRegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/members")
public class MemberController {
    private final MemberService memberService;

    @PostMapping(path = "/register")
    public ResponseEntity<ApiResponse<?>> register(
            @Valid @RequestBody MemberRegisterRequest memberRegisterRequest
    ) {
        memberService.register(memberRegisterRequest);

        return ResponseEntity.ok(
                ApiResponse.ok("회원가입이 완료되었습니다.")
        );
    }
}
