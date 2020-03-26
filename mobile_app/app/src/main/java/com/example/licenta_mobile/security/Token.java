package com.example.licenta_mobile.security;

public class Token {
    private static String jwtToken;

    public static void setJwtToken(String token){
        jwtToken = token;
    }

    public static String getJwtToken(){
        return jwtToken;
    }
}
