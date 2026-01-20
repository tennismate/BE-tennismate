package com.example.tennismate.court.application.service;

import com.example.tennismate.court.dto.response.KakaoAddressResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class KakaoAddressService {

    @Value("${kakao.api.key}") // properties에 넣은 키를 가져옵니다.
    private String apiKey;

    private final WebClient webClient;

    public KakaoAddressService(WebClient.Builder webClientBuilder) {
        // 기본 URL을 카카오 API 주소로 설정합니다.
        this.webClient = webClientBuilder
                .baseUrl("https://dapi.kakao.com/v2/local/search/address.json")
                .build();
    }

    public KakaoAddressResponse.Document getCoordinates(String address) {
        log.info("카카오 API 주소 변환 요청: {}", address);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("query", address).build()) // ?query=주소
                .header("Authorization", "KakaoAK " + apiKey) // "KakaoAK 키" 형식 필수!
                .retrieve()
                .bodyToMono(KakaoAddressResponse.class)
                .map(response -> {
                    if (response.getDocuments() != null && !response.getDocuments().isEmpty()) {
                        return response.getDocuments().get(0); // 검색 결과 중 첫 번째 항목 반환
                    }
                    return null;
                })
                .block(); // 일단은 결과를 기다렸다가 받는 방식으로 처리합니다.
    }
}