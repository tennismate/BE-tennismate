package com.example.tennismate.member.entity;

import com.example.tennismate.global.entity.BaseEntity;
import com.example.tennismate.global.enums.MemberRole;
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

    @Column(name = "role", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MemberRole role;

    public static Member of(String email, String password, String nickname, String phoneNumber, Integer age, String profileImageUrl, String coverImageUrl, MemberRole role) {
        return new Member(email, password, nickname, phoneNumber, age, profileImageUrl, coverImageUrl, role);
    }
}
