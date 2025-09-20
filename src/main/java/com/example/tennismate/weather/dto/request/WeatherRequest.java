package com.example.tennismate.weather.dto.request;

import lombok.Data;



// 위도 경도를
@Data
public class WeatherRequest {
    private Double latitude;
    private Double longitude;
}
