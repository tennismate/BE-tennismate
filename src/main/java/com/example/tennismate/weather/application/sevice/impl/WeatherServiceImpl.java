package com.example.tennismate.weather.application.sevice.impl;

import com.example.tennismate.weather.application.sevice.WeatherService;
import com.example.tennismate.weather.dto.request.WeatherRequest;
import com.example.tennismate.weather.dto.response.WeatherResponse;
import com.example.tennismate.weather.util.ApiResponse;
import com.example.tennismate.weather.util.GpsToGridConverter;
import com.example.tennismate.weather.util.Item;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Slf4j
@Service
public class WeatherServiceImpl implements WeatherService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${weather.api.service-key}")
    private String serviceKey;

    public WeatherServiceImpl(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0");
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        this.webClient = webClientBuilder
                .uriBuilderFactory(factory)
                .baseUrl("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0")
                .build();
        this.objectMapper = objectMapper;
    }

    @Override
    public WeatherResponse getWeatherInfo(WeatherRequest weatherRequest) {

        //위도경도를 격자좌표로 변환
        GpsToGridConverter.Point grid = GpsToGridConverter.convert(weatherRequest.getLatitude(), weatherRequest.getLongitude());
        int nx = grid.x;
        int ny = grid.y;

        //API 호출을 위한 base_date/time 계산
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        String baseTime = getBaseTime(time);

        //basetime 00시 이전이면 basedate 하루 전으로 설정
        String baseDate = baseTime.equals("2300")
                ? date.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                : date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        //webclient로 api호출
        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/getVilageFcst")
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("pageNo", "1")
                        .queryParam("numOfRows", "100") // 필요한 만큼 데이터 수를 조정
                        .queryParam("dataType", "JSON")
                        .queryParam("base_date", baseDate)
                        .queryParam("base_time", baseTime)
                        .queryParam("nx", nx)
                        .queryParam("ny", ny)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();


        //Json 응답 파싱 및 데이터 가공
        try {
            ApiResponse apiResponse = objectMapper.readValue(response, ApiResponse.class);
            List<Item> items = apiResponse.response.body.items.item;

            // 현재 시간과 가장 가까운 예보 찾기
            String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH00"));

            // 기온(TMP), 강수형태(PTY), 하늘상태(SKY) 필터링
            String temp = filterAndGetValue(items, "TMP");
            String precipitationType = filterAndGetValue(items, "PTY");
            String skyStatus = filterAndGetValue(items, "SKY");

            String weatherStatus = getWeatherStatus(skyStatus, precipitationType);
            String weatherIcon = getWeatherIcon(weatherStatus);


            return WeatherResponse.builder()
                    .todayWeatherStatus(weatherStatus + " (기온: " + temp + "°C)")
                    .todayWeatherIcon(weatherIcon)
                    .build();

        } catch (JsonProcessingException | NullPointerException e) {
            log.error("Failed to parse weather API response", e);
            // 예외 발생 시 기본 응답 또는 에러 응답 반환
            return WeatherResponse.builder()
                    .todayWeatherStatus("날씨 정보를 가져올 수 없습니다.")
                    .todayWeatherIcon("❓")
                    .build();
        }


    }

    // 현재 시간에 맞는 base_time 계산 (API 제공 시간에 맞춤)
    private String getBaseTime(LocalTime time) {
        int hour = time.getHour();
        if (hour < 2 || (hour == 2 && time.getMinute() <= 10)) {
            return "2300";
        } else if (hour < 5 || (hour == 5 && time.getMinute() <= 10)) {
            return "0200";
        } else if (hour < 8 || (hour == 8 && time.getMinute() <= 10)) {
            return "0500";
        } else if (hour < 11 || (hour == 11 && time.getMinute() <= 10)) {
            return "0800";
        } else if (hour < 14 || (hour == 14 && time.getMinute() <= 10)) {
            return "1100";
        } else if (hour < 17 || (hour == 17 && time.getMinute() <= 10)) {
            return "1400";
        } else if (hour < 20 || (hour == 20 && time.getMinute() <= 10)) {
            return "1700";
        } else if (hour < 23 || (hour == 23 && time.getMinute() <= 10)) {
            return "2000";
        } else {
            return "2300";
        }
    }

    // 원하는 카테고리와 시간에 맞는 예보 값 찾기
    private String filterAndGetValue(List<Item> items, String category) {
        return items.stream()
                .filter(item -> item.category.equals(category))
                .map(item -> item.fcstValue)
                .findFirst()
                .orElse("정보 없음");
    }

    // 날씨 상태 코드를 텍스트로 변환
    private String getWeatherStatus(String sky, String pty) {
        // PTY(강수형태) 코드: 0(없음), 1(비), 2(비/눈), 3(눈), 4(소나기)
        // SKY(하늘상태) 코드: 1(맑음), 3(구름많음), 4(흐림)

        switch (pty) {
            case "0": // 강수 없음
                switch (sky) {
                    case "1":
                        return "맑음";
                    case "3":
                        return "구름 많음";
                    case "4":
                        return "흐림";
                    default:
                        return "정보 없음";
                }
            case "1":
                return "비";
            case "2":
                return "비/눈";
            case "3":
                return "눈";
            case "4":
                return "소나기";
            default:
                return "정보 없음";
        }
    }

    // 날씨 상태에 맞는 아이콘 반환
    private String getWeatherIcon(String weatherStatus) {
        switch (weatherStatus) {
            case "맑음":
                return "☀️";
            case "구름 많음":
                return "☁️";
            case "흐림":
                return "🌫️";
            case "비":
                return "🌧️";
            case "비/눈":
                return "🌨️";
            case "눈":
                return "❄️";
            case "소나기":
                return "🌦️";
            default:
                return "❓";
        }
    }
}


