package com.Auth.Service;

import com.Auth.Data.User;
import com.Auth.JWT.TokenManager;
import com.Auth.Repository.IUserRepository;
import com.Auth.Utility.AuthResult;
import com.Auth.Utility.StatusAuth;
import jakarta.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

/**
 * Servizio per la gestione dell'autenticazione degli utenti.
 */
@Service("AuthService")
public class AuthService
{
    private final IUserRepository userRepository;
    private final TokenManager tokenManager;

    public AuthService(IUserRepository userRepository, TokenManager tokenManager)
    {
        this.userRepository = userRepository;
        this.tokenManager = tokenManager;
    }

    /**
     * Registra un nuovo utente con i dettagli forniti.
     *
     * @param username Il nome utente del nuovo utente.
     * @param name Il nome del nuovo utente.
     * @param password La password del nuovo utente.
     * @param surname Il cognome del nuovo utente.
     * @param email L'email del nuovo utente.
     * @return Un oggetto AuthResult contenente lo stato e il token (se la registrazione ha successo).
     */
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

    /**
     * Effettua il login dell'utente con le credenziali fornite.
     *
     * @param identifier Nome utente o email dell'utente.
     * @param password La password dell'utente.
     * @return Un oggetto AuthResult contenente lo stato e il token (se il login ha successo).
     */
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

    /**
     * Verifica se il token è valido e restituisce il nome utente associato.
     *
     * @param token Il token da verificare.
     * @return Il nome utente associato al token, o null se il token non è valido.
     */
    public String getUsernameFromToken(String token)
    {
         String username = tokenManager.verifyToken(token);
         if (username == null) return null;
         return username;
    }

    public User getUserInfo(String username)
    {
        if (username == null) return null;
        return userRepository.findByUsername(username);
    }

    /**
     * Associa una casa a un utente.
     *
     * @param username Il nome utente dell'utente a cui associare la casa.
     * @param houseCode Il codice della casa da associare.
     * @return Una risposta HTTP che indica il risultato dell'operazione.
     */
    public Response linkHouseToUser(String username, String houseCode){
        // Recupera l'utente dal repository per evitare duplicati
        User existingUser = userRepository.findByUsername(username);
        if (existingUser == null) {
            return Response.status(Response.Status.NOT_FOUND).build(); // L'utente non esiste
        }

        // Verifica se l'utente ha già una casa associata
        if (existingUser.getHouseUser() != null) {
            // Se l'utente ha già una casa associata, verifica se il codice fa match con quello criptato
            if (BCrypt.checkpw(houseCode, existingUser.getHouseUser())) {
                return Response.status(Response.Status.OK).entity("Login nella casa").build(); // La casa è già associata
            }
            return Response.status(Response.Status.CONFLICT).build(); // L'utente ha già una casa associata
        }
        String hashedHouseCode = BCrypt.hashpw(houseCode, BCrypt.gensalt());
        userRepository.setHouseUser(username, hashedHouseCode);
        return Response.ok().build(); // Operazione riuscita
    }
}