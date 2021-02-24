package com.example.licenta_mobile.rest

import com.example.licenta_mobile.dto.MoneyTransferDto
import com.example.licenta_mobile.dto.UserDataDto
import retrofit2.Call
import retrofit2.http.*

interface UserDataService {

    @GET("/users/{id}")
    fun getUserDetails(@Path("id") id: Int): Call<UserDataDto>

    @POST("/users/addMoney")
    fun transferMoney(@Body moneyTransferDto: MoneyTransferDto): Call<Void>
}