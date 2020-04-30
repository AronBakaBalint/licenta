package com.example.licenta_mobile.dto;

import com.example.licenta_mobile.model.UserData;

public class UserDataDto {

    private String username;
    private Double currentSold;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public Double getCurrentSold() {
        return currentSold;
    }

    public void setCurrentSold(Double currentSold) {
        this.currentSold = currentSold;
    }
}
