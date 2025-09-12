package com.example.tennismate.infrastructure.security.filter;

import com.example.tennismate.infrastructure.security.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. 요청 헤더에서 토큰 추출
        String token = resolveToken(request);

        // 2. 토큰 유효성 검증
        if (StringUtils.hasText(token) && jwtProvider.validateToken(token)) {
            // 3. 토큰에서 사용자 정보 추출
            Claims info = jwtProvider.getUserInfoFromToken(token);
            String email = info.get("email", String.class);

            // 4. 인증 처리
            try {
                // TODO : 4-1. 토큰의 이메일로 DB 에서 사용자 정보 조회

                // TODO : 4-2. 인증 객체 생성

                // TODO : 4-3. SecurityContext 에 인증 정보 저장


            } catch (Exception e) {

            }
        }



        // 5. 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    /**
     * 요청 헤더에서 "Bearer " 접두사를 제거하고 순수 토큰을 반환하는 메서드
     * @param request : 요청 헤더
     * @return : 순수 토큰 or null 반환
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
