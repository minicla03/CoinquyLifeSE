package com.coinquyteam.gateway.JWT;

        import org.springframework.http.HttpHeaders;
        import org.springframework.http.server.reactive.ServerHttpRequest;
        import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
        import org.springframework.security.core.Authentication;
        import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
        import org.springframework.stereotype.Component;
        import reactor.core.publisher.Mono;

        import java.util.Collections;

        /**
         * Filtro di autenticazione JWT per Spring WebFlux.
         * Estende AuthenticationWebFilter e utilizza un TokenManager per validare i token JWT.
         */
        @Component
        public class JWTAuthenticationFilter extends AuthenticationWebFilter {

            /**
             * Costruttore che inizializza il filtro con un JWTReactiveAuthenticationManager.
             * Imposta anche il converter per estrarre l'autenticazione dalla richiesta.
             *
             * @param tokenManager gestore dei token JWT
             */
            public JWTAuthenticationFilter(TokenManager tokenManager) {
                super(new JWTReactiveAuthenticationManager(tokenManager));
                this.setServerAuthenticationConverter(exchange -> extractAuthentication(exchange.getRequest(), tokenManager));
            }

            /**
             * Estrae l'autenticazione dalla richiesta HTTP controllando l'header Authorization.
             * Se il token è valido, restituisce un oggetto Authentication.
             * @param request richiesta HTTP
             * @param tokenManager gestore dei token JWT
             * @return Mono\<Authentication\> se il token è valido, altrimenti Mono vuoto
             */
            private Mono<Authentication> extractAuthentication(ServerHttpRequest request, TokenManager tokenManager) {
                String header = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                if (header != null && header.startsWith("Bearer ")) {
                    String token = header.substring(7);
                    System.out.println("Extracted token: " + token);
                    String username = tokenManager.verifyToken(token);
                    System.out.println("Extracted username: " + username);
                    if (username != null) {
                        // Passa il token come credentials, non null
                        return Mono.just(new UsernamePasswordAuthenticationToken(username, token, Collections.emptyList()));
                    }
                }
                return Mono.empty();
            }

        }