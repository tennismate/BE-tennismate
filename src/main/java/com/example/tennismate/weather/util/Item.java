package com.example.tennismate.weather.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    public String baseDate;
    public String baseTime;
    public String category;
    public String fcstDate;    //fcst = forecast
    public String fcstTime;
    public String fcstValue;
    public int nx;
    public int ny;
}