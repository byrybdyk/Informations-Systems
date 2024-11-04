package com.byrybdyk.lb1.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Отключить CSRF для упрощения запросов
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.GET, "/**").permitAll()       // Разрешить все GET запросы
                        .requestMatchers(HttpMethod.POST, "/**").authenticated()  // Требовать авторизацию для POST
                        .requestMatchers(HttpMethod.PUT, "/**").authenticated()   // Требовать авторизацию для PUT
                        .requestMatchers(HttpMethod.DELETE, "/**").authenticated() // Требовать авторизацию для DELETE
                        .anyRequest().permitAll()  // Разрешить все остальные запросы
                )
                .httpBasic();  // Включить базовую авторизацию

        return http.build();
    }
}
