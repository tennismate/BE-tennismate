package com.example.tennismate.member.entity;

import com.example.tennismate.global.entity.BaseEntity;
import com.example.tennismate.member.enums.MemberRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {
    @Column(name = "email", unique = true, nullable = false, length = 50)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", unique = true, nullable = false, length = 50)
    private String nickname;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "age")
    private Integer age;

    @Column(name = "profile_image_url", columnDefinition = "TEXT")
    private String profileImageUrl;

    @Column(name = "cover_image_url", columnDefinition = "TEXT")
    private String coverImageUrl;

    @Column(name = "refresh_token", columnDefinition = "TEXT")
    private String refreshToken;

    @Column(name = "role", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MemberRole role;

    /**
     * refresh Token 을 쉽게 업데이트하기 위한 비즈니스 메서드
     * @param refreshToken : 리프레시 토큰
     */
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    /**
     * Member 객체 생성을 위한 of 메서드
     * @param email : 사용자 이메일 ID
     * @param password : 패스워드
     * @param nickname : 닉네임
     * @param phoneNumber : 휴대폰 번호
     * @param age : 나이
     * @param profileImageUrl : 프로필 이미지 url
     * @param coverImageUrl : 커버 이미지 url
     * @param refreshToken : 리프레시 토큰
     * @param role : 사용자 권한
     * @return : Member 객체
     */
    public static Member of(String email, String password, String nickname, String phoneNumber, Integer age, String profileImageUrl, String coverImageUrl, String refreshToken, MemberRole role) {
        return new Member(email, password, nickname, phoneNumber, age, profileImageUrl, coverImageUrl, refreshToken, role);
    }
}
