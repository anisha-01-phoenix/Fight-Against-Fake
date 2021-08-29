package com.example.fightagainstfake;

public class UserModel {

    private String id, name, username,deviceToken;

    public UserModel() {
    }

    public String getId() {
        return id;
    }

    public UserModel(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
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

    public UserModel(String id, String name, String username, String deviceToken) {
        this.id = id;
        this.name = name;
        this.username = username;

    }
}
