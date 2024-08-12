package com.example.stylish.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

import com.example.stylish.security.JwtAuthenticationFilter;
import com.example.stylish.security.RateLimiterFilter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RateLimiterFilter rateLimiterFilter;

    public SecurityConfig(@Lazy JwtAuthenticationFilter jwtAuthenticationFilter, RateLimiterFilter rateLimiterFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.rateLimiterFilter = rateLimiterFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(requests -> requests
                .requestMatchers("/admin/**").hasRole("admin")
                .requestMatchers("/api/1.0/user/profile", "/api/1.0/order/checkout").authenticated()
                .anyRequest().permitAll())
            .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(rateLimiterFilter, AuthorizationFilter.class)
            .addFilterBefore(jwtAuthenticationFilter, AuthorizationFilter.class)
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(authenticationEntryPoint()) // if the user was not authenticated
                .accessDeniedHandler(accessDeniedHandler()) // if the user does not have the admin role
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> response.sendRedirect("/profile.html");
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> response.sendRedirect("/accessDenied.html");
    }
}

