package com.example.tennismate.weather.application.sevice.impl;

import com.example.tennismate.weather.application.sevice.WeatherService;
import com.example.tennismate.weather.dto.request.WeatherRequest;
import com.example.tennismate.weather.dto.response.WeatherResponse;
import org.springframework.stereotype.Service;


@Service
public class WeatherServiceImpl implements WeatherService {

    @Override
    public WeatherResponse getWeatherInfo(WeatherRequest  weatherRequest) {
        //TODO : 실제 날씨데이터가공/API호출로


        //일단 임시데이터 반환
        return WeatherResponse.builder()
                .todayWeatherStatus("테니스치기 좋은 날")
                .todayWeatherIcon(" ^-^")
                .build();
    }

}
