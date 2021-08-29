package com.example.fightagainstfake;

public class UserModel {

    private String id, name, username,DeviceToken;

    public UserModel(String id, String name, String username, String DeviceToken) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.DeviceToken = DeviceToken;
    }

    public UserModel() {
    }

    public String getDeviceToken() {
        return DeviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        DeviceToken = deviceToken;
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
