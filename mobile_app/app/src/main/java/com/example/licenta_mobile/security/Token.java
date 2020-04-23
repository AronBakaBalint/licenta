package com.example.licenta_mobile.security;

import android.util.Base64;

import java.util.Arrays;


public class Token {
    private static String jwtToken;

    public static void setJwtToken(String token){
        jwtToken = token;
    }

    public static String getJwtToken(){
        return jwtToken;
    }

    public static int getUserId(){
        byte[] byteArray = Base64.decode(jwtToken.getBytes(),0);
        String decodedString = new String(byteArray);
        decodedString = decodedString.substring(decodedString.indexOf("\"jti\":"));
        decodedString = decodedString.substring(0, decodedString.indexOf("}"));
        decodedString = decodedString.replace("\"jti\":", "").replace("\"", "");
        System.out.println(decodedString);
        return Integer.parseInt(decodedString);
    }
}
