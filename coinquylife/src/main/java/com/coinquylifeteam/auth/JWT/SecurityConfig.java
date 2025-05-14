package com.coinquylifeteam.auth.JWT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    // Inietta il TokenManager per la gestione dei token JWT
    @Autowired
    private TokenManager tokenManager;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disattiva CSRF se usi token
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/rest/auth/login", "/rest/auth/register").permitAll()
                        .requestMatchers("/rest/house/create").authenticated()
                        .requestMatchers("/rest/house/loginHouse").authenticated()
                        .requestMatchers("/rest/client/house").authenticated()
                        .anyRequest().permitAll() // Permetti tutte le altre richieste
                )
                .addFilterBefore(new JWTAuthenticationFilter(tokenManager), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
