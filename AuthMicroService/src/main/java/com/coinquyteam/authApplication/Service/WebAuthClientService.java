package com.coinquyteam.authApplication.Service;

import com.coinquyteam.authApplication.Data.User;
import com.coinquyteam.authApplication.JWT.TokenManager;
import com.coinquyteam.authApplication.Repository.IUserRepository;
import com.coinquyteam.authApplication.Utility.AuthResult;
import com.coinquyteam.authApplication.Utility.StatusAuth;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

/**
 * Classe per gestire le comunicazioni esterne con il servizio
 * separato dal service di auth per motivi si separazione delle
 * responsabilità.
 */
@Service
public class WebAuthClientService
{
    private final IUserRepository userRepository;
    private final TokenManager tokenManager;

    public WebAuthClientService(IUserRepository userRepository, TokenManager tokenManager)
    {
        this.userRepository = userRepository;
        this.tokenManager = tokenManager;
    }

    public AuthResult linkHouseToUser(String token, String houseCode)
    {
        if (token == null || houseCode == null) {
            return new AuthResult(StatusAuth.INVALID_CREDENTIALS, "Invalid input");
        }
        User user = getUserInfo(getUsernameFromToken(token));
        if (user == null) {
            return new AuthResult(StatusAuth.USER_NOT_FOUND, "User not found");
        }

        if (user.getHouseUser() != null) {

            if(BCrypt.checkpw(houseCode, user.getHouseUser())) {
                return new AuthResult(StatusAuth.SUCCESS, "User linked to his house");
            }
            return new AuthResult(StatusAuth.USER_ALREADY_LINKED, "User already linked to a house");
        }

        try
        {
            String hashedHouseCode = BCrypt.hashpw(houseCode, BCrypt.gensalt());
            userRepository.setHouseUser(user.getUsername(), hashedHouseCode);
            return new AuthResult(StatusAuth.SUCCESS, "House linked successfully");
        }
        catch (Exception e)
        {
            return new AuthResult(StatusAuth.LINKED_ERROR, "Failed to link house");
        }
    }

    private String getUsernameFromToken(String token)
    {
        return tokenManager.verifyToken(token);
    }

    private User getUserInfo(String username)
    {
        assert username != null;
        return userRepository.findByUsername(username);
    }

}