package com.example.tennismate.court.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourtCreateRequest {
    private String courtName;
    private String roadAddress;     // "서울 동작구 상도로 369"
    private String detailAddress;   // "정문 테니스장"
    private String courtImageUrl;
    private String reservationUrl;
    private boolean parkingAvailable;
}