package com.example.tennismate.weather.dto.response;

import lombok.*;
import java.util.*;

@Data
@Builder
public class WeatherResponse {
    private String todayWeatherStatus;
    private String todayWeatherIcon;
    //private List<DailyWeatherStatus> weatherResponses;  다른 응답줄지는 일단 보류.. 점수? 가능성 ?

    @Data
    @Builder
    public static class DailyWeatherStatus {
        private String day;
        private String weatherIcon;
    }

}

