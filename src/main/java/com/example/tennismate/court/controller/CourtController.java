package com.example.tennismate.court.controller;

import com.example.tennismate.court.application.service.CourtService;
import com.example.tennismate.court.dto.request.CourtCreateRequest;
import com.example.tennismate.court.dto.response.CourtResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // 외부 요청을 받아서 json형태로 응답주는 컨트롤러야!
@RequiredArgsConstructor // Final 필드인 courtservice 주입받는 생성자 만듬
@RequestMapping("/api/courts") // 컨트롤러가 담당할 대표주소(url)정함
public class CourtController {

    private final CourtService courtService;

    @PostMapping // post요청이 왔을때 연결
    public Long createCourt(@RequestBody CourtCreateRequest request) {
        // 클라이언트가 보낸 json데이터를 DTO(CourtCreateRequest)로 변환해서 넣어줌
        // 이제 컨트롤러가 클라이언트의 JSON을 DTO에 담아 서비스로 넘겨줍니다.
        return courtService.saveCourt(request); // 코트 정보받아서 디비에 저장하고 생성된 Id 가져옴
    }

    @GetMapping // 모든 코트 목록 조회
    public List<CourtResponse> getAllCourts() {
        return courtService.findAllCourts();
    }

    @GetMapping("/nearby") // 내 주변 코트 찾기
    public ResponseEntity<List<CourtResponse>> getNearbyCourts(
            @RequestParam Double lat,
            @RequestParam Double lon,
            @RequestParam(defaultValue = "3.0") Double distance) {
        return ResponseEntity.ok(courtService.getNearbyCourts(lat, lon, distance));
    }
}