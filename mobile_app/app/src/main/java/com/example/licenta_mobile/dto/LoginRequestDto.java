package com.example.licenta_mobile.dto;

import com.example.licenta_mobile.rest.LoginService;
import com.example.licenta_mobile.rest.RestClient;
import com.google.gson.annotations.SerializedName;

public class LoginRequestDto {

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    public LoginRequestDto(String username, String password){
        this.username = username;
        this.password = password;
    }
}
