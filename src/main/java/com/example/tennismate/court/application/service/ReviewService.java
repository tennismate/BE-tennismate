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
        Long savedReviewId = reviewRepository.save(review).getId();

        updateCourtAverageRating(court);

        return savedReviewId;
    }

    // court 테이블에 average rating 칼럼을 따로 둬서 후기가 달릴때마다 계산된 값 저장하게 한다
    private void updateCourtAverageRating(Court court) {
        // 해당 코트에 달린 후기 디비에서 가져옴
        List<Review> reviews = reviewRepository.findByCourtIdOrderByCreatedAtDesc(court.getId());

        //자바 stream api로 평균값 계산
        double average = reviews.stream()
                .mapToInt(Review::getRating) // 리뷰객체에서 점수만 가져옴
                .average()
                .orElse(0.0); //후기 없으면 0

        // 평균값을 court엔티티에 반영
        court.updateAverageRating(average);
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

    // 리뷰 삭제
    @Transactional
    public void deleteReview(Long reviewId) {
        // 1. 삭제할 리뷰가 있는지 확인, 없으면 에러발생
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다. ID: " + reviewId));

        // 2. 코트 정보가져옴
        Court court = review.getCourt();

        // 3. 리뷰 삭제
        reviewRepository.delete(review);

        // 4. 리뷰가 삭제된 상태에서 평균 평점을 다시 계산해서 업데이트
        updateCourtAverageRating(court);
    }
}