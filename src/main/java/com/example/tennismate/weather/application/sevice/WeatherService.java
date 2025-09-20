package com.example.tennismate.weather.application.sevice;

import com.example.tennismate.weather.dto.request.WeatherRequest;
import com.example.tennismate.weather.dto.response.WeatherResponse;

public interface WeatherService {
    WeatherResponse getWeatherInfo(WeatherRequest weatherRequest);
}
