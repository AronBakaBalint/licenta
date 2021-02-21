package com.example.licenta_mobile.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.licenta_mobile.dto.UserDataDto
import com.example.licenta_mobile.rest.RestClient
import com.example.licenta_mobile.rest.UserDataService
import com.example.licenta_mobile.security.Token
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object UserData {
    @JvmStatic var userId: Int = 0
    @JvmStatic var currentSold = 0.0
    @JvmStatic lateinit var userName: String
    @JvmStatic lateinit var email: String
    @JvmStatic val userDataService = RestClient.client!!.create(UserDataService::class.java)!!

    @JvmStatic
    fun update() {
        val call = userDataService.getUserDetails("Bearer " + Token.jwtToken, userId)
        call.enqueue(object : Callback<UserDataDto> {
            override fun onResponse(call: Call<UserDataDto>, response: Response<UserDataDto>) {
                if (response.isSuccessful) {
                    userName = response.body()!!.username
                    email = response.body()!!.email
                    currentSold = response.body()!!.currentSold
                }
            }

            override fun onFailure(call: Call<UserDataDto>, t: Throwable) {
                println(t.message)
                call.cancel()
            }
        })
    }
}