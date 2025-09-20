package com.example.tennismate.infrastructure.security.filter;

import com.example.tennismate.global.response.ApiResponse;
import com.example.tennismate.infrastructure.security.domain.CustomUserDetails;
import com.example.tennismate.infrastructure.security.jwt.JwtProvider;
import com.example.tennismate.member.application.port.MemberRepositoryPort;
import com.example.tennismate.member.dto.request.MemberLoginRequest;
import com.example.tennismate.member.dto.response.MemberLoginResponse;
import com.example.tennismate.member.entity.Member;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

// NOTE : UsernamePasswordAuthenticationFilter 는 정해진 HTML 폼 방식에만 사용하고,
//  우리처럼 JSON 으로 데이터를 주고받는 API 서버에서는
//  더 유연한 부모 템플릿인 AbstractAuthenticationProcessingFilter 를 상속받아 직접 구현하는 것이 올바른 방법입니다.
@Slf4j
public class CustomLoginFilter extends AbstractAuthenticationProcessingFilter {
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;
    private final MemberRepositoryPort memberRepositoryPort;

    // 1. 생성자
    public CustomLoginFilter(JwtProvider jwtProvider, ObjectMapper objectMapper, MemberRepositoryPort memberRepositoryPort) {
        // 2. 부모 클래스 생성자 호출
        // 로그인 URI 에 대한 요청을 가로챌 수 있도록 설정
        super(new AntPathRequestMatcher("/api/v1/members/login", "POST"));
        this.jwtProvider = jwtProvider;
        this.objectMapper = objectMapper;
        this.memberRepositoryPort = memberRepositoryPort;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.info("로그인 시도");

        // 1. 요청 본문의 JSON 을 MemberLoginRequest DTO 로 변환
        MemberLoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), MemberLoginRequest.class);

        // 2. 인증용 객체(AuthenticationToken) 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.email(),
                loginRequest.password()
        );

        // 3. AuthenticationManager 에게 인증 위임 및 결과 반환
        // 미리 설정해 둔 의존관게 : UserDetailsServiceImpl, password encoder
        // Spring Security 가 찾아서 회원 정보 비교해주는 과정
        // 성공 또는 실패 여부 반환
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공 및 토큰 발행");

        // 1. Principal 을 CustomUserDetails 로 변환
        CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();

        // 2. CustomUserDetails 에서 직접 사용자 정보 추출
        Long id = userDetails.getId();
        String email = userDetails.getEmail();
        String nickname = userDetails.getNickname();
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .orElseThrow(() -> new ServletException("User role not found"))
                .getAuthority();

        // 3. Jwt Provider 를 사용해 토큰 생성 (Access Token 에 id 포함)
        String accessToken = jwtProvider.createAccessToken(id, email, role);
        String refreshToken = jwtProvider.createRefreshToken(email);

        // 3-1. DB 에서 사용자 정보 조회
        Member member = memberRepositoryPort.findMemberByEmail(email)
                .orElseThrow(() -> new ServletException("Could not find member"));

        // 3-2. dirty checking 또는 명시적 save 를 위해 Refresh Token 업데이트
        member.updateRefreshToken(refreshToken);
        memberRepositoryPort.save(member);

        // 4. 실제 데이터를 담아 응답 DTO 생성
        MemberLoginResponse loginResponse = MemberLoginResponse.of(email, nickname, role, accessToken, refreshToken);
        ApiResponse<MemberLoginResponse> apiResponse = ApiResponse.ok("로그인에 성공하였습니다", loginResponse);

        // 5. JSON 응답 전송
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패");

        // 1. HTTP 상태 코드를 401 unauthorized 로 설정
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // 2. 응답 컨텐츠 타입을 JSON 으로 설정
        response.setContentType("application/json;charset=UTF-8");

        // 3. 실패 메시지를 담은 응답 전송
        response.getWriter().write(objectMapper.writeValueAsString(
                ApiResponse.error(401, "이메일 또는 비밀번호가 일치하지 않습니다.")
        ));

    }
}
