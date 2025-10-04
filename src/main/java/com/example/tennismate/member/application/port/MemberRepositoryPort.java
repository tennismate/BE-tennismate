package com.example.tennismate.member.application.port;

import com.example.tennismate.member.entity.Member;

import java.util.Optional;

public interface MemberRepositoryPort {
    /**
     * 새로운 Member 를 등록(가입) 할 때 사용하는 메서드
     * @param member : 신규 Member 객체
     */
    void register(Member member);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<Member> findMemberByEmail(String email);

    /**
     * Member 객체를 저장하거나 업데이트 시 사용하는 메서드
     * @param member : create, update 할 Member 객체
     */
    void save(Member member);
}
