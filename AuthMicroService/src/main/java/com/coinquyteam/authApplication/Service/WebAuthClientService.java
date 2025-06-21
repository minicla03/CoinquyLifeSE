package com.coinquyteam.authApplication.Service;
import com.coinquyteam.authApplication.Data.User;
import com.coinquyteam.authApplication.Repository.IUserRepository;
import com.coinquyteam.authApplication.Utility.AuthResult;
import com.coinquyteam.authApplication.Utility.StatusAuth;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

@Service("WebAuthClientService")
public class WebAuthClientService {

    // Repository per accedere ai dati utente
    private final IUserRepository userRepository;
    // WebClient per chiamate REST verso altri microservizi
    private final WebClient webClient;

    // Costruttore con injection delle dipendenze
    public WebAuthClientService(IUserRepository userRepository, WebClient webClient) {
        this.userRepository = userRepository;
        this.webClient = webClient;
    }

    /**
     * Collega un utente a una casa tramite houseCode.
     * @param token token JWT dell'utente
     * @param houseCode codice della casa da collegare
     * @return AuthResult con lo stato dell'operazione
     */
    public AuthResult linkHouseToUser(String token, String houseCode) {
        if (token == null || houseCode == null) {
            return new AuthResult(StatusAuth.INVALID_CREDENTIALS, "Invalid input");
        }

        // Estrae lo username dal token tramite chiamata REST
        String username = getUsernameFromTokenViaRest(token);
        if (username == null) {
            return new AuthResult(StatusAuth.USER_NOT_FOUND, "User not found or invalid token");
        }

        // Recupera le informazioni dell'utente dal repository
        User user = getUserInfo(username);
        if (user == null) {
            return new AuthResult(StatusAuth.USER_NOT_FOUND, "User not found");
        }

        // Se l'utente è già collegato a una casa
        if (user.getHouseUser() != null) {
            // Verifica se il codice casa corrisponde a quello già collegato
            if (BCrypt.checkpw(houseCode, user.getHouseUser())) {
                return new AuthResult(StatusAuth.SUCCESS, "User linked to his house");
            }
            return new AuthResult(StatusAuth.USER_ALREADY_LINKED, "User already linked to a house");
        }

        // Collega l'utente alla casa salvando il codice hashato
        try {
            String hashedHouseCode = BCrypt.hashpw(houseCode, BCrypt.gensalt());
            userRepository.setHouseUser(user.getUsername(), hashedHouseCode);
            return new AuthResult(StatusAuth.SUCCESS, "House linked successfully");
        } catch (Exception e) {
            return new AuthResult(StatusAuth.LINKED_ERROR, "Failed to link house");
        }
    }

    /**
     * Estrae lo username dal token tramite chiamata REST a un altro microservizio.
     * @param token token JWT
     * @return username se valido, altrimenti null
     */
    private String getUsernameFromTokenViaRest(String token) {
        try {
            Map<String, String> response = webClient.post()
                    .uri("http://localhost:8080/gateway/verify-token")
                    .bodyValue(Map.of("token", token))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            // La risposta JSON dovrebbe essere tipo {"username": "theUser"}
            return response != null ? response.get("username") : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Recupera le informazioni dell'utente dal repository.
     * @param username username dell'utente
     * @return User se trovato, altrimenti null
     */
    private User getUserInfo(String username) {
        return userRepository.findByUsername(username);
    }
}