package com.coinquyteam.authApplication.Utility;

import com.coinquyteam.authApplication.Data.User;

import java.util.List;

public class UserResult {

    private StatusAuth statusAuth;
    private List<User> users;

    public UserResult(StatusAuth statusAuth, List<User> users) {
        this.statusAuth = statusAuth;
        this.users = users;
    }


    public StatusAuth getStatusAuth() {
        return statusAuth;
    }

    public void setStatusAuth(StatusAuth statusAuth) {
        this.statusAuth = statusAuth;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
