package com.example.tennismate.weather.controller;

import com.example.tennismate.weather.application.sevice.WeatherService;
import com.example.tennismate.weather.dto.request.WeatherRequest;
import com.example.tennismate.weather.dto.response.WeatherResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping

public class WeatherController {
    private final WeatherService weatherService;

    @PostMapping
    public WeatherResponse getWeatherInfo(@RequestBody WeatherRequest weatherRequest) {
        return weatherService.getWeatherInfo(weatherRequest);
    }



}
