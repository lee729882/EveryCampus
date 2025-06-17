package com.everycampus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests().anyRequest().permitAll() // ← 전부 허용
            .and()
            .formLogin().disable() // ← 기본 로그인폼 비활성화
            .logout().disable();   // ← 로그아웃도 필요 없으면 비활성화

        return http.build();
    }
}
