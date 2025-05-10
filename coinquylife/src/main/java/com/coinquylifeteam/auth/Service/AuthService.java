package com.coinquylifeteam.auth.Service;

import com.coinquylifeteam.auth.Data.User;
import com.coinquylifeteam.auth.JWT.TokenManager;
import com.coinquylifeteam.auth.Repository.IUserRepository;
import com.dbclient.MongoDBManager;
import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
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

    public boolean register(String username, String password) {
        if (userRepository.findByUsername(username) != null)
        {
            return false;
        }
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        userRepository.insert(new User(username, hashed));
        return true;
    }

    public String login(String username, String password) {
        User user=userRepository.findByUsername(username);
        if (user == null) return null;

        if (BCrypt.checkpw(password, user.getPassword())) {
            return tokenManager.generateToken(username);
        }
        return null;
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