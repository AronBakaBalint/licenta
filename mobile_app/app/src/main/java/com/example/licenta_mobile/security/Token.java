package com.example.licenta_mobile.security;

import android.util.Base64;

import com.example.licenta_mobile.model.UserData;

import java.util.Arrays;


public class Token {
    private static String jwtToken;

    public static void setJwtToken(String token){
        jwtToken = token;
        int userId = getUserId();
        UserData.setUserId(userId);
    }

    public static String getJwtToken(){
        return jwtToken;
    }

    private static int getUserId(){
        byte[] byteArray = Base64.decode(jwtToken.getBytes(),0);
        String decodedString = new String(byteArray);
        decodedString = decodedString.substring(decodedString.indexOf("\"jti\":"));
        decodedString = decodedString.substring(0, decodedString.indexOf("}"));
        decodedString = decodedString.replace("\"jti\":", "").replace("\"", "");
        return Integer.parseInt(decodedString);
    }
}
