package com.example.tennismate.court.controller;

import com.example.tennismate.court.application.service.CourtService;
import com.example.tennismate.court.dto.response.CourtResponse;
import com.example.tennismate.court.entity.Court;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController //외부 요청을 받아서 json형태로 응답주는 컨트롤러야!
@RequiredArgsConstructor  //Final필드인 courtservice 주입받는 생성자 만듬
@RequestMapping("/api/courts") //컨트롤러가 당담할 대표주소(url)정함
public class CourtController {

    private final CourtService courtService;

    @PostMapping//post요청이 왔을때 연결
    public Long createCourt(@RequestBody Court court) { //클라이언트가 보낸 json데이터를 court로 반환해서 넣어줌
        return courtService.saveCourt(court); //코트 정보받아서 디비에 저장하고 Id 가져옴
    }

    @GetMapping
    public List<CourtResponse> getAllCourts(){
        return courtService.findAllCourts();
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<CourtResponse>> getNearbyCourts(
            @RequestParam Double lat,
            @RequestParam Double lon,
            @RequestParam(defaultValue = "3.0") Double distance){
        return ResponseEntity.ok(courtService.getNearbyCourts(lat, lon, distance));
    }
}