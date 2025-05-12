package com.coinquylifeteam.auth.Service;

import com.coinquylifeteam.auth.Data.User;
import com.coinquylifeteam.auth.JWT.TokenManager;
import com.coinquylifeteam.auth.Repository.IUserRepository;
import com.coinquylifeteam.auth.Utility.AuthResult;
import com.coinquylifeteam.auth.Utility.StatusAuth;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class AuthService
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

    public AuthResult login(String identifier, String password)
    {
        User user = userRepository.findByUsername(identifier);
        if (user == null) {
            user = userRepository.findByEmail(identifier);
        }

        if (user == null) return new AuthResult(StatusAuth.USER_NOT_FOUND, null);

        if (BCrypt.checkpw(password, user.getPassword())) {
            return new AuthResult(StatusAuth.SUCCESS, tokenManager.generateToken(user.getUsername()));
        }
        return new AuthResult(StatusAuth.INVALID_CREDENTIALS, null);
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