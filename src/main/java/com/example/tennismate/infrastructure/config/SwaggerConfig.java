package com.example.tennismate.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "TennisMate API 명세서",
                description = "테니스메이트 프로젝트 API 명세서 입니다.",
                version = "v1"
        )
)
@Configuration
public class SwaggerConfig {
        @Bean
        public OpenAPI openAPI() {
                // 1. SecurityScheme 이름 정의
                String jwtSchemeName = "jwtAuth";

                // 2. API 요청 헤더에 인증 정보 포함을 위한 SecurityRequirement 객체 생성
                SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);

                // TODO : 3. SecuritySchemes 객체 설정

        }
}
