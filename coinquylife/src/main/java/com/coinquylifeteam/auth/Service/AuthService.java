package com.coinquylifeteam.auth.Service;

import com.coinquylifeteam.auth.Data.User;
import com.coinquylifeteam.auth.JWT.TokenManager;
import com.coinquylifeteam.auth.Repository.IUserRepository;
import com.coinquylifeteam.auth.Utility.AuthResult;
import com.coinquylifeteam.auth.Utility.StatusAuth;
import jakarta.validation.Payload;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;

import java.util.Collections;

@Service
public class AuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>
{
    private final IUserRepository userRepository;
    private final TokenManager tokenManager;

    public AuthService(IUserRepository userRepository, TokenManager tokenManager)
    {
        this.userRepository = userRepository;
        this.tokenManager = tokenManager;
    }

    public AuthResult register(String username, String name, String password, String surname, String email)
    {
        if (userRepository.findByUsername(username) != null) {
            return new AuthResult(StatusAuth.USER_ALREADY_EXISTS, null);
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User(username, name, hashedPassword, surname, email);
        userRepository.save(user);

        return new AuthResult(StatusAuth.SUCCESS, tokenManager.generateToken(username));
    }

    public AuthResult login(String username, String password)
    {
        User user=userRepository.findByUsername(username);
        if (user == null) return new AuthResult(StatusAuth.USER_NOT_FOUND, null);

        if (BCrypt.checkpw(password, user.getPassword())) {
            return new AuthResult(StatusAuth.SUCCESS, tokenManager.generateToken(username));
        }
        return new AuthResult(StatusAuth.INVALID_CREDENTIALS, null);
    }

    public AuthResult verifyTokenGoogle(String idToken)
    {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
                .Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        try {
            GoogleIdToken tokenGoogle = verifier.verify(idToken);
            if (tokenGoogle != null) {
                Payload payload= tokenGoogle.getPayload();

                String email = payload.getEmail();
                String name = (String) payload.get("given_name");
                String surname = (String) payload.get("family_name");
                String username = (String) payload.get("name");
                String sub = payload.getSubject(); // Google ID

                User user= userRepository.findByEmail(email);
                if(user == null)
                {
                    userRepository.save(new User(sub, username, name, null, surname, email););
                }
            }
            else
            {
                throw new RuntimeException("Token non valido");
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException("Errore nella verifica del token", e);
        }
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = new DefaultOAuth2UserService().loadUser(userRequest);
        String email = user.getAttribute("email");

        // Salva o aggiorna utente nel DB
        userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(new User(email)));

        return user;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.authorizeRequests(a -> a.antMatchers("/", "/error", "/webjars/**").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .oauth2Login();
    }

    public boolean isAuthenticated(String token)
    {
        String username = tokenManager.verifyToken(token);
        if (username == null) return false;
        return true;

        //DA MODIFICARE
        //return utenti.find(new Document("username", username)).first() != null;
    }
}