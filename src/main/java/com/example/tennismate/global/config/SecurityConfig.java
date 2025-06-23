package com.example.tennismate.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    private static final String[] SWAGGER_WHITELIST = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Swagger 관련 경로
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(SWAGGER_WHITELIST).permitAll()
                .anyRequest().authenticated()
        );

        // form login 해제
        http.formLogin(AbstractHttpConfigurer::disable);

        // csrf 해제
        http.csrf(AbstractHttpConfigurer::disable);

        // http basic 해제
        http.httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
