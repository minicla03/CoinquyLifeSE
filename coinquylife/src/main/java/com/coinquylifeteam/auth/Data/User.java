package com.coinquylifeteam.auth.Data;

import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "users")
public class User
{
    @Id
    @Field("_id")
    private String id_user;

    @Field("username")
    private String username;

    @Field("name")
    private String name;

    @Field("password")
    private String password;

    @Field("surname")
    private String surname;

    @Field("email")
    private String email;

    @Field("level")
    private int level;

    @Field("total_points")
    private int total_points;

    @Field("language")
    private String language;

    @Field("profile_image")
    private byte[] profileImage;

    @Field("house_user")
    private String houseUser;

    public User() { }

    public User(String username, String name, String password, String surname, String email)
    {
        this.username = username;
        this.name = name;
        this.password = password;
        this.surname = surname;
        this.email = email;
        this.level = 0;
        this.total_points = 0;
        this.profileImage = null;
        this.houseUser=null;
    }

    public User(String username, String pass)
    {
        this.username = username;
        this.password = pass;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getTotal_points() {
        return total_points;
    }

    public void setTotal_points(int total_points) {
        this.total_points = total_points;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public byte[] getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }

    public String getHouseUser() {
        return houseUser;
    }

    public void setHouseUser(String houseUser) {
        this.houseUser = houseUser;
    }
}





