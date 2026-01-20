package com.example.tennismate.court.application.service;

import com.example.tennismate.court.dto.response.CourtResponse;
import com.example.tennismate.court.entity.Court;
import com.example.tennismate.court.repository.CourtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) //디비 트랜잯현을 시작
public class CourtService {

    private final CourtRepository courtRepository; //디비 접근통로

    @Transactional //여기서는 read only해제
    public Long saveCourt(Court court) {
        Court savedCourt = courtRepository.save(court);
        return savedCourt.getId();
    }

    @Transactional(readOnly = true)
    public List<CourtResponse> getNearbyCourts(Double lat, Double lon, Double distance) {
        Double distanceThreshold = 3.0; // 3km 기준
        List<Court> courts = courtRepository.findNearbyCourts(lat, lon, distance);

        return courts.stream()
                .map(CourtResponse::from) // Entity를 DTO로 변환
                .collect(Collectors.toList());
    }

    // 모든 코트 목록 조회
    public List<CourtResponse> findAllCourts() {
        return courtRepository.findAll().stream()
                .map(CourtResponse::new) // 또는 CourtResponse::from
                .collect(Collectors.toList());
    }
}