package com.example.models.users;

import org.json.JSONObject;

public class UserModel {
    private int id;
    private String username;
    private String phoneNumber;  // Change to string
    private String password;
    private JSONObject address;

    // Constructors, getters, and setters
    public UserModel() {}

    public UserModel(int id, String username, String phoneNumber, String password, JSONObject address) {
        this.id = id;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.address = address;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public JSONObject getAddress() {
        return address;
    }

    public void setAddress(JSONObject address) {
        this.address = address;
    }
}
