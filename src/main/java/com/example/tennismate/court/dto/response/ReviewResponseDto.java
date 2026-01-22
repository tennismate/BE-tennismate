package com.example.tennismate.court.dto.response;

import com.example.tennismate.court.entity.Review;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter; // 포맷을 위해 추가

@Getter
public class ReviewResponseDto {
    private Long id;
    private int rating;
    private String content;
    private String createdAt;  // string vs localdatetime 자꾸 오류생겨서 일단 string으로 함
    //localdatetime은 단순글자가 아닌 시간정보를 가진 객체이므로 계산가능하고 프론트엔드의 자유도가 있다>>기회되면 change시도

    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.rating = review.getRating();
        this.content = review.getContent();

        // 날짜가 null일 경우를 대비해 안전하게 변환
        if (review.getCreatedAt() != null) {
            this.createdAt = review.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } else {
            this.createdAt = "";
        }
    }
}