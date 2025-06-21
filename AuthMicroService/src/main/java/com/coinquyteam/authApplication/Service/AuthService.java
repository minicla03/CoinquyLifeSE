package com.coinquyteam.authApplication.Service;

import com.coinquyteam.authApplication.Data.User;
import com.coinquyteam.authApplication.Repository.IUserRepository;
import com.coinquyteam.authApplication.Utility.AuthResult;
import com.coinquyteam.authApplication.Utility.StatusAuth;
import com.coinquyteam.authApplication.Utility.UserResult;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service("AuthService")
public class AuthService {
    private final IUserRepository userRepository;
    private final WebClient webClient;

    // Costruttore del servizio di autenticazione
    public AuthService(IUserRepository userRepository, WebClient webClient) {
        this.userRepository = userRepository;
        this.webClient = webClient;
    }

    /**
     * Registra un nuovo utente se username o email non sono già presenti.
     * Restituisce un AuthResult con lo stato e il token generato.
     */
    public AuthResult register(String username, String name, String password, String surname, String email) {
        if (userRepository.findByUsername(username) != null || userRepository.findByEmail(email) != null) {
            return new AuthResult(StatusAuth.USER_ALREADY_EXISTS, null);
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User(username, name, hashedPassword, surname, email);
        userRepository.save(user);

        String token = generateTokenViaRest(username);
        return new AuthResult(StatusAuth.SUCCESS, token);
    }

    /**
     * Effettua il login tramite username o email e password.
     * Restituisce un AuthResult con lo stato e il token se le credenziali sono corrette.
     */
    public AuthResult login(String identifier, String password) {
        User user = userRepository.findByUsername(identifier);
        if (user == null) {
            user = userRepository.findByEmail(identifier);
        }
        if (user == null) {
            return new AuthResult(StatusAuth.USER_NOT_FOUND, null);
        }
        if (BCrypt.checkpw(password, user.getPassword())) {
            String token = generateTokenViaRest(user.getUsername());
            return new AuthResult(StatusAuth.SUCCESS, token);
        }
        return new AuthResult(StatusAuth.INVALID_CREDENTIALS, null);
    }

    /**
     * Richiama un servizio REST per generare un token JWT per l'utente.
     */
    private String generateTokenViaRest(String username) {
        Map responseMap = webClient.post()
                .uri("http://localhost:8080/gateway/generate-token")
                .bodyValue(Map.of("username", username))
                .retrieve()
                .bodyToMono(Map.class)
                .block(); // blocca fino a ricevere risposta

        return (String) responseMap.get("token");
    }

    //NON USATO
    public UserResult getUserByHouseId(String houseId) {

        List<User> users = userRepository.findAll()
                .stream()
                .filter(u -> BCrypt.checkpw(houseId, u.getHouseUser()))
                .toList();

        if (users != null) {
            return new UserResult(StatusAuth.USERS_FOUNDED, users);
        }
        return new UserResult(StatusAuth.USERS_NOT_FOUND, null);
    }

    /**
     * Restituisce la lista degli utenti (coinquilini) associati a uno specifico houseId.
     */
    public List<User> getCoinquilinibyHouseId(String houseId)
    {
        List<User> list = userRepository.findAll().stream()
                .filter(u -> BCrypt.checkpw(houseId, u.getHouseUser()))
                .toList();
        list.forEach(System.out::println);
        return list;
    }
}