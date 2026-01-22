package com.example.tennismate.court.application.service;

import com.example.tennismate.court.dto.request.ReviewRequestDto;
import com.example.tennismate.court.dto.response.ReviewResponseDto;
import com.example.tennismate.court.entity.Court;
import com.example.tennismate.court.entity.Review;
import com.example.tennismate.court.repository.CourtRepository;
import com.example.tennismate.court.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CourtRepository courtRepository;

    @Transactional
    public Long createReview(Long courtId, ReviewRequestDto dto) {

        // 1. 해당 코트가 존재하는지 확인
        Court court = courtRepository.findById(courtId)
                .orElseThrow(() -> new IllegalArgumentException("해당 테니스장이 존재하지 않습니다. ID: " + courtId));

        // 2. DTO 데이터를 바탕으로 Review 엔티티 생성
        Review review = Review.builder()
                .court(court)
                .rating(dto.getRating())
                .content(dto.getContent())
                .build();

        // 3. 리포지토리를 통해 DB에 저장
        return reviewRepository.save(review).getId();
    }

    // ReviewService.java 내 조회 메서드
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviewsByCourt(Long courtId) {
        // 1. 해당 코트의 리뷰들을 DB에서 가져옴
        List<Review> reviews = reviewRepository.findByCourtIdOrderByCreatedAtDesc(courtId);

        // 2. Review 엔티티 리스트를 ReviewResponseDto 리스트로 변환 (Stream API 활용)
        return reviews.stream()
                .map(ReviewResponseDto::new)
                .collect(Collectors.toList());
    }
}