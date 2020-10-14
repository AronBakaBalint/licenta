package com.example.licenta_mobile.rest

import com.example.licenta_mobile.dto.MessageDto
import com.example.licenta_mobile.dto.RegistrationDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RegistrationService {

    @POST("/register")
    @Headers("Content-Type: application/json")
    fun register(@Body registrationDto: RegistrationDto): Call<MessageDto>
}