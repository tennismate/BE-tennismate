package com.example.tennismate.court.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // 정의한 데이터말고는 무시
public class KakaoAddressResponse {

    private List<Document> documents; // 카카오는 결과를 'documents'라는 리스트에 사용 (아래 정의)

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Document {
        private String x; // 경도 (longitude)
        private String y; // 위도 (latitude)
    }
}