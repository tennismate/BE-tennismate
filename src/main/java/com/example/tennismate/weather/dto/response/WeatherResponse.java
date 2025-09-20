package com.example.tennismate.weather.dto.response;

import lombok.*;
import java.util.*;

@Data
@Builder
public class WeatherResponse {
    private String todayWeatherStatus;
    private String todayWeatherIcon;
    private List<DailyWeatherStatus> weatherResponses;

    @Data
    @Builder
    public static class DailyWeatherStatus {
        private String day;
        private String weatherIcon;
    }

}

