package com.example.tennismate.member.application.port;

import com.example.tennismate.member.entity.Member;

public interface MemberRepositoryPort {
    void register(Member member);

    boolean existsByEmail(String email);
}
