package com.example.tennismate.infrastructure.security.service;

import com.example.tennismate.infrastructure.security.domain.CustomUserDetails;
import com.example.tennismate.member.application.port.MemberRepositoryPort;
import com.example.tennismate.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepositoryPort memberRepositoryPort;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Port 를 통해 DB 에서 이메일로 회원 정보를 조회
        Member member = memberRepositoryPort.findMemberByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일을 찾을 수 없습니다 : " + email));

        // 2. 조회된 회원 정보를 바탕으로 Spring Security 가 사용하는 UserDetails 객체를 생성하여 반환
        return new CustomUserDetails(member);
    }
}
