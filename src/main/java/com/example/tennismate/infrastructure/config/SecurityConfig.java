package com.example.tennismate.infrastructure.config;

import com.example.tennismate.global.constants.AuthEndPoints;
import com.example.tennismate.infrastructure.security.filter.CustomLoginFilter;
import com.example.tennismate.infrastructure.security.filter.JwtAuthorizationFilter;
import com.example.tennismate.infrastructure.security.jwt.JwtProvider;
import com.example.tennismate.member.application.port.MemberRepositoryPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserDetailsService userDetailsService;
    private final MemberRepositoryPort memberRepositoryPort;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CustomLoginFilter customLoginFilter() throws Exception {
        CustomLoginFilter customLoginFilter = new CustomLoginFilter(jwtProvider, objectMapper, memberRepositoryPort);
        customLoginFilter.setAuthenticationManager(authenticationManager());
        return customLoginFilter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtProvider, userDetailsService, objectMapper);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF, Form Login, HTTP BASIC 비활성화
        http.csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        // 세션을 사용하지 않으므로, STATELESS 설정
        http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // URL 별 경로 권한 설정
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(AuthEndPoints.SWAGGER_ENDPOINTS).permitAll()
                // 회원가입, 로그인 경로 오픈
                .requestMatchers(HttpMethod.POST, AuthEndPoints.AUTH_PERMIT_ENDPOINTS).permitAll()
                .anyRequest().authenticated()
        );

        // CustomLoginFilter 를 UsernamePasswordAuthenticationFilter 자리에 끼워넣음
        http.addFilterAt(customLoginFilter(), UsernamePasswordAuthenticationFilter.class);

        // JwtAuthorizationFilter 를 로그인 필터 앞단에 배치
        http.addFilterBefore(jwtAuthorizationFilter(), CustomLoginFilter.class);

        return http.build();
    }
}
