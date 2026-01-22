package com.example.tennismate.court.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewRequestDto {

    @Min(1) @Max(5)
    private int rating;   // 별점 (1~5)

    @NotBlank(message = "후기 내용을 입력해주세요.")
    private String content; //후기 내용

    //지금은 API 테스트를 위해 넣어둠 >>원래는 토큰에서 정보를 꺼내서 사용하고 dto에서는 memberid가 빠지게 된다
    private Long memberId;
}