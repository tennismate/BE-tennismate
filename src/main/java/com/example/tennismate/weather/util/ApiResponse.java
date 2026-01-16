package com.example.tennismate.weather.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

// 이 파일의 유일한 public 클래스
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse {
    public Response response;


    // 같은 파일에 있으므로 public ㄴㄴ
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        public Body body;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body {
        public Items items;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Items {
        public List<Item> item; // 위에서 만든 Item 클래스를 사용
    }
}