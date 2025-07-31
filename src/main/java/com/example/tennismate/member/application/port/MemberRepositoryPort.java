package com.example.tennismate.member.application.port;

import com.example.tennismate.member.entity.Member;

import java.util.Optional;

public interface MemberRepositoryPort {
    void register(Member member);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<Member> findMemberByEmail(String email);
}
