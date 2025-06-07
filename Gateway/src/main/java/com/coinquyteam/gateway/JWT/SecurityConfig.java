package com.coinquyteam.gateway.JWT;

import jakarta.ws.rs.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.WebFilter;

@Configuration
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                         JWTAuthenticationFilter jwtAuthenticationFilter) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(HttpMethod.POST, "/gateway/generate-token").permitAll()
                        .pathMatchers("http://localhost:8083/**").authenticated()
                        .pathMatchers("http://localhost:8080/House/**").authenticated()
                        .pathMatchers("http://localhost:8084/**").authenticated()
                        .pathMatchers("http://localhost:8080/Expense/**").authenticated()
                        .pathMatchers("http://localhost:8085/**").authenticated()
                        .pathMatchers("http://localhost:8080/Dashboard/**").authenticated()
                        .pathMatchers("http://localhost:8086/**").authenticated()
                        .pathMatchers("http://localhost:8080/Shift/**").authenticated()
                        .pathMatchers("http://localhost:8087/**").authenticated()
                        .pathMatchers("http://localhost:8080/Rank/**").authenticated()
                        .pathMatchers("/actuator/**").permitAll()
                        .anyExchange().permitAll()
                )
                .addFilterBefore(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

}