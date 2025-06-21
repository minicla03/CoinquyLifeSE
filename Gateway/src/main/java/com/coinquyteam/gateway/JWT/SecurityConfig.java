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

    // Definisce il filtro di sicurezza principale per l'applicazione gateway
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                         JWTAuthenticationFilter jwtAuthenticationFilter) {
        return http
                // Disabilita la protezione CSRF (Cross-Site Request Forgery)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                // Disabilita il form di login predefinito
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                // Disabilita l'autenticazione HTTP Basic
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                // Configura le regole di autorizzazione per le richieste HTTP
                .authorizeExchange(exchange -> exchange
                        // Permette a chiunque di accedere all'endpoint per generare il token
                        .pathMatchers(HttpMethod.POST, "/gateway/generate-token").permitAll()
                        // Richiede autenticazione per le seguenti risorse
                        .pathMatchers("/House/**").authenticated()
                        .pathMatchers("/Expense/**").authenticated()
                        .pathMatchers("/Dashboard/**").authenticated()
                        .pathMatchers("/Shift/**").authenticated()
                        .pathMatchers("/Rank/**").authenticated()
                        // Permette l'accesso libero agli endpoint actuator
                        .pathMatchers("/actuator/**").permitAll()
                        // Permette l'accesso a tutte le altre richieste
                        .anyExchange().permitAll()
                )
                // Aggiunge il filtro di autenticazione JWT prima del filtro di autenticazione standard
                .addFilterBefore(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                // Costruisce la catena dei filtri di sicurezza
                .build();
    }

}