package com.coinquylifeteam.auth.Controller;

import com.coinquylifeteam.auth.Data.User;
import com.coinquylifeteam.auth.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("api/auth")
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

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        boolean result = authService.register(user.getUsername(), user.getPassword());
        if (result) {
            return ResponseEntity.ok("Registration successful");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
        }
    }
}
