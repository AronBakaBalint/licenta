package com.example.licenta_mobile.rest

import com.example.licenta_mobile.dto.UserDataDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserDataService {

    @GET("/users")
    fun getUserDetails(): Call<UserDataDto>

    @POST("/users/addMoney")
    fun transferMoney(@Body amount: Double): Call<Void>
}