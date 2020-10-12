package com.example.licenta_mobile.security

import com.auth0.android.jwt.JWT
import com.example.licenta_mobile.model.UserData

object Token {
    var jwtToken: String? = null
        set(token) {
            val parsedJWT = JWT(token!!)
            val userId = parsedJWT.getClaim("jti")
            UserData.userId = userId.asInt()!!
            field = token
        }
}