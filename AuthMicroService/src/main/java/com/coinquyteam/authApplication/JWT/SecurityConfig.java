package com.coinquyteam.authApplication.JWT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                .csrf(csrf -> csrf.disable()) // Disabilita CSRF perché usi token JWT
                .authorizeHttpRequests(auth -> auth
                        // Permetti qualsiasi file statico (qualsiasi estensione e sottocartella)
                        .requestMatchers("/**").permitAll()

                        // Permetti senza autenticazione login e registrazione
                        .requestMatchers("/rest/auth/login", "/rest/auth/register").permitAll()

                        // Proteggi le API che richiedono autenticazione
                        .requestMatchers(
                                "/rest/house/create",
                                "/rest/house/loginHouse",
                                "/rest/client/house"
                        ).authenticated()

                        // Tutte le altre richieste sono libere
                        .anyRequest().permitAll()
                )
                .addFilterBefore(new JWTAuthenticationFilter(tokenManager), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
