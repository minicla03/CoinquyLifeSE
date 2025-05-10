package com.coinquylifeteam.auth.Controller;

import com.coinquylifeteam.auth.Service.AuthService;
import com.coinquylifeteam.auth.Utility.AuthResult;
import com.coinquylifeteam.auth.Utility.StatusAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> requestData) {
        String username = requestData.get("username");
        String password = requestData.get("password");
        AuthResult result = authService.login(username, password);

        if (result.getStatusAuth() == StatusAuth.SUCCESS) {
            return ResponseEntity.ok(Map.of("message", "Login successful"));
        } else if (result.getStatusAuth() == StatusAuth.USER_NOT_FOUND) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found"));
        } else if (result.getStatusAuth() == StatusAuth.INVALID_CREDENTIALS) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid credentials"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "An error occurred"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody Map<String, String> requestData) {
        String username = requestData.get("username");
        String name = requestData.get("name");
        String password = requestData.get("password");
        String surname = requestData.get("surname");
        String email = requestData.get("email");

        AuthResult result = authService.register(username, name, password, surname, email);

        if (result.getStatusAuth() == StatusAuth.SUCCESS) {
            return ResponseEntity.ok(Map.of("message", "Registration successful"));
        } else if (result.getStatusAuth() == StatusAuth.USER_ALREADY_EXISTS) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "User already exists"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "An error occurred"));
        }
    }
}
