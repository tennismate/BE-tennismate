package com.example.tennismate.court.controller;

import com.example.tennismate.court.dto.request.ReviewRequestDto;
import com.example.tennismate.court.application.service.ReviewService;
import com.example.tennismate.court.dto.response.ReviewResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courts")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{courtId}/reviews")
    public ResponseEntity<Long> addReview(
            @PathVariable Long courtId,
            @RequestBody ReviewRequestDto dto
    ) {
        // 서비스 호출 (현재 Member 기능 미연동으로 memberId는 내부에서 처리하거나 null 유지)
        Long reviewId = reviewService.createReview(courtId, dto);

        return ResponseEntity.ok(reviewId);
    }

    // ReviewController.java에 추가
    @GetMapping("/{courtId}/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getReviews(@PathVariable Long courtId) {
        List<ReviewResponseDto> reviews = reviewService.getReviewsByCourt(courtId);
        return ResponseEntity.ok(reviews);
    }
}