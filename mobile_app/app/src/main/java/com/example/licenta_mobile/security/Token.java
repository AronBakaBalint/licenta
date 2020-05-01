package com.example.licenta_mobile.security;

import android.util.Base64;

import com.example.licenta_mobile.model.UserData;

import java.util.Arrays;


public class Token {
    private static String jwtToken;

    public static void setJwtToken(String token){
        Integer userId = Integer.parseInt(token.substring(0, token.indexOf(";")));
        jwtToken = token.substring(token.indexOf(";")+1);
        UserData.setUserId(userId);
    }

    public static String getJwtToken(){
        return jwtToken;
    }

}
