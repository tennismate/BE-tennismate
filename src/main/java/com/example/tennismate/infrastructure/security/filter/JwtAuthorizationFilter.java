package com.example.tennismate.infrastructure.security.filter;

import com.example.tennismate.global.response.ApiResponse;
import com.example.tennismate.infrastructure.security.jwt.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;
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
                // 4-1. 토큰의 이메일로 DB 에서 사용자 정보 조회
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                // 4-2. 인증 객체 생성
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // 4-3. SecurityContext 에 인증 정보 저장
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);

            } catch (Exception e) {
                log.error("Cannot set user authentication: {}", e.getMessage());
                SecurityContextHolder.clearContext();   // 1. 보안 컨텍스트 초기화 - 혹시 모를 비정상적인 인증 정보가 남아있지 않도록, 현재 요청의 보안 정보를 깨끗하게 비운다.

                // 2. HTTP 상태 코드 설정
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                // 3. 응답 형식 및 내용 설정
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(objectMapper.writeValueAsString(
                        ApiResponse.error(401, "인증 정보가 유효하지 않습니다.")
                ));

                return;
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
