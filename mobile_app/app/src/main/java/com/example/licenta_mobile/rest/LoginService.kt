package com.example.licenta_mobile.rest

import com.example.licenta_mobile.dto.JwtTokenDto
import com.example.licenta_mobile.dto.LoginRequestDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginService {

    @POST("/login")
    @Headers("Content-Type: application/json")
    fun authenticate(@Body loginRequestDto: LoginRequestDto): Call<JwtTokenDto>
}