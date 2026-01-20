package com.example.tennismate.weather.controller;

import com.example.tennismate.weather.application.sevice.WeatherService;
import com.example.tennismate.weather.dto.request.WeatherRequest;
import com.example.tennismate.weather.dto.response.WeatherResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService; //생성자를 통해 WeatherService를 주입받는다

    @GetMapping  //Get요청을 처리하는 메서드
    public ResponseEntity<WeatherResponse> getWeather(@ModelAttribute WeatherRequest weatherRequest) {
        // 1. 서비스 레이어의 getWeatherInfo 메소드를 호출하여 날씨 정보를 가져옵니다.
        WeatherResponse weatherInfo = weatherService.getWeatherInfo(weatherRequest);

        // 2. 가져온 날씨 정보를 ResponseEntity에 담아 클라이언트에게 반환합니다.
        return ResponseEntity.ok(weatherInfo);
    }



}
