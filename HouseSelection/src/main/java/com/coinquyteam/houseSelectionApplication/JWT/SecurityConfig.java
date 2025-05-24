package com.coinquyteam.houseSelectionApplication.JWT;

import org.apache.catalina.filters.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private TokenManager tokenManager;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())   // Abilita CORS
                .csrf(csrf -> csrf.disable())// Disabilita CSRF perché usi token JWT
                .httpBasic(basic -> basic.disable()) // Disabilita l'autenticazione di base
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/rest/house/loginHouse", "/rest/house/create").permitAll() // Richieste di login e creazione casa richiedono autenticazione
                        // Tutte le altre richieste sono libere
                        .anyRequest().permitAll()
                )
                .addFilterBefore(new JWTAuthenticationFilter(tokenManager), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
