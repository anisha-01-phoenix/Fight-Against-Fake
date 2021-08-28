package com.example.fightagainstfake;

public class UserModel {

    private String id, name, username,token;

    public UserModel(String id, String name, String username, String token) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.token = token;
    }

    public UserModel() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
