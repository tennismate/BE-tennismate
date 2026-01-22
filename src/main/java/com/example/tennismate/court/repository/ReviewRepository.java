package com.example.tennismate.court.repository;

import com.example.tennismate.court.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // 기본 CRUD 기능(save, findById 등)은 JpaRepository로 다 ㄱㄴ
    // ReviewRepository.java에 추가
    List<Review> findByCourtIdOrderByCreatedAtDesc(Long courtId);
}