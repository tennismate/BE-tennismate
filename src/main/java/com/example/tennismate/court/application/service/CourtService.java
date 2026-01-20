package com.example.tennismate.court.application.service;

import com.example.tennismate.court.dto.request.CourtCreateRequest;
import com.example.tennismate.court.dto.response.CourtResponse;
import com.example.tennismate.court.entity.Address;
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
    private final KakaoAddressService kakaoAddressService; //카카오 api 서비스 주



    @Transactional // 쓰기 작업이므로 readOnly 해제
    public Long saveCourt(CourtCreateRequest request) {
        // 1. 카카오 API를 통해 주소를 좌표로 변환
        var coordinates = kakaoAddressService.getCoordinates(request.getRoadAddress());

        // 2. 좌표가 없을 경우를 대비해 기본값 설정
        Double lat = (coordinates != null) ? Double.parseDouble(coordinates.getY()) : 0.0;
        Double lon = (coordinates != null) ? Double.parseDouble(coordinates.getX()) : 0.0;

        // 3. Address 엔티티 생성 (여기에 변환된 좌표를 넣습니다)
        Address address = Address.builder()
                .roadAddress(request.getRoadAddress())
                .detailAddress(request.getDetailAddress())
                .latitude(lat)
                .longitude(lon)
                .build();

        // 4. Court 엔티티 생성 및 Address 연결
        Court court = Court.builder()
                .courtName(request.getCourtName())
                .address(address)
                .courtImageUrl(request.getCourtImageUrl())
                .reservationUrl(request.getReservationUrl())
                .parkingAvailable(request.isParkingAvailable())
                .build();

        // 5. CascadeType.ALL 덕분에 court만 저장해도 address가 같이 저장됩니다.
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