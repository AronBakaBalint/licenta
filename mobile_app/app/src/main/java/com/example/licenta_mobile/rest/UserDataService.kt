package com.example.licenta_mobile.rest

import com.example.licenta_mobile.dto.MoneyTransferDto
import com.example.licenta_mobile.dto.UserDataDto
import retrofit2.Call
import retrofit2.http.*

interface UserDataService {
    @GET("/users/{id}")
    @Headers("Content-Type: application/json")
    fun getUserDetails(@Header("Authorization") auth: String?, @Path("id") id: Int?): Call<UserDataDto>

    @POST("/users/addMoney")
    @Headers("Content-Type: application/json")
    fun transferMoney(@Header("Authorization") auth: String?, @Body moneyTransferDto: MoneyTransferDto?): Call<Void?>?
}