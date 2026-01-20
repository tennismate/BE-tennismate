package com.example.tennismate.court.dto.response;

import com.example.tennismate.court.entity.Court;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CourtResponse {
    private Long id;
    private String courtName;
    private String roadAddress;
    private Double latitude;
    private Double longitude;
    private Boolean parkingAvailable;

    //엔티티를 dto로 바꿀때 From이라는 정적 메서드를 만드는 방식 사용
    public static CourtResponse from(Court court) {
        return new CourtResponse(court);
    }

    // 엔티티를 DTO로 변환하는 생성자
    public CourtResponse(Court court) {
        this.id = court.getId();
        this.courtName = court.getCourtName();

        // 롬복 @Getter는 boolean 필드에 대해 isParkingAvailable()을 생성합니다.
        this.parkingAvailable = court.isParkingAvailable();

        if (court.getAddress() != null) {
            this.roadAddress = court.getAddress().getRoadAddress();
            this.latitude = court.getAddress().getLatitude();
            this.longitude = court.getAddress().getLongitude();
        }
    }
}