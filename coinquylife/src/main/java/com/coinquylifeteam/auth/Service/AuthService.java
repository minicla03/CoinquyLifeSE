package com.coinquylifeteam.auth.Service;

import com.coinquylifeteam.auth.Data.User;
import com.coinquylifeteam.auth.JWT.TokenManager;
import com.coinquylifeteam.auth.Repository.IUserRepository;
import com.coinquylifeteam.auth.Utility.AuthResult;
import com.coinquylifeteam.auth.Utility.StatusAuth;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

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

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException
    {
        OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(userRequest);
        String email = oauth2User.getAttribute("email");
        String username = oauth2User.getAttribute("name");
        String name = oauth2User.getAttribute("name");
        String surname = oauth2User.getAttribute("family_name");
        String password = "GoogleOAuth2"; // Placeholder password, not used in this case
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

       User user=userRepository.findByEmail(email);
       if(user==null)
       {
            User newUser = new User(username, name, hashedPassword, surname, email);
            newUser.setEmail(email);
            userRepository.save(newUser);
        }
        return oauth2User;
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