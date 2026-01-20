package com.example.tennismate.court.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "road_address", nullable = false)
    private String roadAddress; // 도로명 주소

    @Column(name = "detail_address")
    private String detailAddress; // 상세 주소

    @Column(nullable = false)
    private Double latitude; // 위도 (GPS 기반 조회용)

    @Column(nullable = false)
    private Double longitude; // 경도 (GPS 기반 조회용)

    @Builder
    public Address(String roadAddress, String detailAddress, Double latitude, Double longitude) {
        this.roadAddress = roadAddress;
        this.detailAddress = detailAddress;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public void updateCoordinates(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}