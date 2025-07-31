package com.example.tennismate.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record MemberLoginRequest(
        @NotBlank(message = "이메일은 필수 입력사항입니다.")
        @Email(message = "이메일 형식을 지켜 입력해주세요.")
        String email,
        @NotBlank(message = "비밀번호는 필수 입력사항입니다.")
        @Length(min = 10, max = 16, message = "비밀번호는 10자 이상, 16자 이하로 입력해주세요")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).+$",
                message = "비밀번호는 영문, 숫자, 특수문자를 모두 포함해야합니다."
        )
        String password
) {
}
