package com.example.tennismate.court.entity;

import com.example.tennismate.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "court_id")
    private Court court; // TennisCourt 대신 기존에 만드신 Court 클래스 사용!

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private int rating;

    @Column(columnDefinition = "TEXT")
    private String content;

    @CreatedDate // 생성시 날짜 자동 주입
    @Column(updatable = false) //생성 후 ㅅ후정불가
    private LocalDateTime createdAt;

    @Builder
    public Review(Court court, Member member, int rating, String content) {
        this.court = court;
        this.member = member;
        this.rating = rating;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }
}