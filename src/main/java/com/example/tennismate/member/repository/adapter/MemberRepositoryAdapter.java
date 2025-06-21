package com.example.tennismate.member.repository.adapter;

import com.example.tennismate.member.application.port.MemberRepositoryPort;
import com.example.tennismate.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryAdapter implements MemberRepositoryPort {
    private final MemberRepository memberRepository;
}
