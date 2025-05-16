package Client;

import Data.User;
import JWT.TokenManager;
import Repository.IUserRepository;
import Utility.AuthResult;
import Utility.StatusAuth;
import org.springframework.stereotype.Service;

/**
 * Classe per gestire le comunicazioni esterne con il servizio
 * separato dal service di auth per motivi si separazione delle
 * responsabilità.
 */
@Service
public class WebAuthClient
{
    private final IUserRepository userRepository;
    private final TokenManager tokenManager;

    public WebAuthClient(IUserRepository userRepository, TokenManager tokenManager)
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

        try
        {
            userRepository.setHouseUser(user.getUsername(), houseCode);
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