package com.coinquylifeteam.auth.Controller;

import com.coinquylifeteam.auth.Data.User;
import com.coinquylifeteam.auth.Service.AuthService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController
{
    @Autowired
    private AuthService authService;

    @GET("/login")
    public Response login(@RequestBody User user)
    {
        boolean result = authService.login(user.getUsername(), user.getPassword());
        if (result) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @POST("/register")
    public Response register(@RequestBody User user) {
        boolean result = authService.register(user.getUsername(), user.getPassword());
        if (result) {
            return ResponseEntity.ok("Registration successful");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
        }
    }
}
