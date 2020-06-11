package com.example.licenta_mobile.security;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.example.licenta_mobile.model.UserData;

public class Token {
    private static String jwtToken;

    public static void setJwtToken(String token) {
        JWT parsedJWT = new JWT(token);
        Claim userId = parsedJWT.getClaim("jti");
        UserData.setUserId(userId.asInt());
        jwtToken = token;
    }

    public static String getJwtToken() {
        return jwtToken;
    }

}
