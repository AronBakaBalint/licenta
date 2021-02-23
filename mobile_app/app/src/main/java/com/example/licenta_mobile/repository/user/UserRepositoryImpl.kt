package com.example.licenta_mobile.repository.user

import com.example.licenta_mobile.activity.main.reservations.LoginResponse
import com.example.licenta_mobile.dto.LoginRequestDto
import com.example.licenta_mobile.model.UserData
import com.example.licenta_mobile.rest.LoginService
import com.example.licenta_mobile.rest.RestClient
import com.example.licenta_mobile.security.Token
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepositoryImpl : UserRepository{

    private val loginService = RestClient.client!!.create(LoginService::class.java)

    override fun login(loginRequestDto: LoginRequestDto, loginResponse: (loginResponse: LoginResponse) -> Unit) {
        val call = loginService.authenticate(loginRequestDto)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()!!
                    if ("false" == responseBody) {
                        loginResponse.invoke(LoginResponse.WRONG_PASSWORD)
                    } else {
                        loginResponse.invoke(LoginResponse.SUCCESS)
                        Token.jwtToken = responseBody
                        UserData.update()
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                loginResponse.invoke(LoginResponse.CONNECTION_ERROR)
                call.cancel()
            }
        })
    }
}