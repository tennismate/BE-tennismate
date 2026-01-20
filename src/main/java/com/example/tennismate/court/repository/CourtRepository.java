package com.example.tennismate.court.repository;

import com.example.tennismate.court.entity.Court;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository //이 인터페이스는 디비랑 연결되는 저장소 객체야!!, spring container에 등록
public interface CourtRepository extends JpaRepository<Court, Long> {

    // 하버사인 공식을 이용한 3km 이내 좌표 조회
    @Query(value = "SELECT c.* FROM court c " +
            "JOIN address a ON c.address_id = a.id " +
            "WHERE (6371 * acos(cos(radians(:lat)) * cos(radians(a.latitude)) * " +
            "cos(radians(a.longitude) - radians(:lon)) + " +
            "sin(radians(:lat)) * sin(radians(a.latitude)))) <= :distance",
            nativeQuery = true)
    List<Court> findNearbyCourts(@Param("lat") Double lat,
                                 @Param("lon") Double lon,
                                 @Param("distance") Double distance);
}