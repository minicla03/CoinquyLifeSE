package com.coinquylifeteam.auth.Utility;

public class AuthResult
{
    private StatusAuth statusAuth;
    private String token;

    public AuthResult(StatusAuth statusAuth, String token) {
        this.statusAuth = statusAuth;
        this.token = token;
    }

    public StatusAuth getStatusAuth() {
        return statusAuth;
    }

    public void setStatusAuth(StatusAuth statusAuth) {
        this.statusAuth = statusAuth;
    }
}
