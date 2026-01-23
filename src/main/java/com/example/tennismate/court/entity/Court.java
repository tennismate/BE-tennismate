package com.example.tennismate.court.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity  //이 클래스는 디비 테이블임을 알려준다,  DB에 court 테이블 자동생성 or 매핑
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //매개변수 없는 기본 생성자 만들어줌
public class Court {

    @Id  //PK지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) //알아서 아이디값 넣어줌
    private Long id; //아이디 저장할 변수

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) //json이 자꾸 프록시 객체 처리못해서..
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) //코트 하나에 주소하나 매핑되는 1대1
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(name = "court_name" ,nullable = false)
    private String courtName; // 코트명

    @Column(columnDefinition = "TEXT")
    private String courtImageUrl; // 사진 URL

    @Column(columnDefinition = "TEXT")
    private String reservationUrl; // 예약 사이트 링크

    @Column(nullable = false)
    private boolean parkingAvailable; // 주차 가능 여부

    // 후순위로 구현할 '별점 평균'을 미리 필드로 두면 조회가 빠릅니다.
    private Double averageRating = 0.0;

    @Builder
    public Court(Address address, String courtName, String courtImageUrl,
                 String reservationUrl, boolean parkingAvailable) {
        this.address = address;
        this.courtName = courtName;
        this.courtImageUrl = courtImageUrl;
        this.reservationUrl = reservationUrl;
        this.parkingAvailable = parkingAvailable;
    }

    public void updateAverageRating(double newAverage) {
        this.averageRating = Math.round(newAverage * 10) / 10.0;
    }
}

