package com.example.licenta_mobile.model

import com.example.licenta_mobile.dto.UserDataDto
import com.example.licenta_mobile.rest.RestClient
import com.example.licenta_mobile.rest.UserDataService
import com.example.licenta_mobile.security.Token
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object UserData {
    @JvmStatic var userId: Int? = null
    @JvmStatic var userName: String? = null
    @JvmStatic var currentSold: Double? = null
    @JvmStatic var email: String? = null
    @JvmStatic val userDataService = RestClient.getClient().create(UserDataService::class.java)

    @JvmStatic
    fun update() {
        val call = userDataService.getUserDetails("Bearer " + Token.getJwtToken(), userId)
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