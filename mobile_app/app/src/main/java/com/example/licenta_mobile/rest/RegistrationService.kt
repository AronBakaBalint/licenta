package com.example.licenta_mobile.rest

import com.example.licenta_mobile.dto.RegistrationDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RegistrationService {

    @POST("/register")
    fun register(@Body registrationDto: RegistrationDto): Call<Int>
}