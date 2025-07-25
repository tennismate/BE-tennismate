package com.example.tennismate.member.repository.adapter;

import com.example.tennismate.member.application.port.MemberRepositoryPort;
import com.example.tennismate.member.entity.Member;
import com.example.tennismate.member.repository.MemberRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryAdapter implements MemberRepositoryPort {
    private final MemberRepository memberRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void register(Member member) {
        memberRepository.save(member);
    }

    @Override
    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }
}
