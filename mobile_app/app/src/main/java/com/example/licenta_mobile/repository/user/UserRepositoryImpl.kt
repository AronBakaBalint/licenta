package com.example.licenta_mobile.repository.user

import com.example.licenta_mobile.activity.login.LoginResponse
import com.example.licenta_mobile.activity.register.RegisterResponse
import com.example.licenta_mobile.dto.LoginRequestDto
import com.example.licenta_mobile.dto.RegistrationDto
import com.example.licenta_mobile.model.UserData
import com.example.licenta_mobile.rest.LoginService
import com.example.licenta_mobile.rest.RegistrationService
import com.example.licenta_mobile.rest.RestClient
import com.example.licenta_mobile.security.Token
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepositoryImpl : UserRepository{

    private val loginService = RestClient.client!!.create(LoginService::class.java)

    private val registrationService = RestClient.client!!.create(RegistrationService::class.java)

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

    override fun register(fullName: String, username: String, email: String, password: String, registerResponse: (response: RegisterResponse) -> Unit) {
        val registrationDto = RegistrationDto(fullName, username, email, password)
        val call = registrationService.register(registrationDto)
        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    if (0 == response.body()!!) {
                        registerResponse.invoke(RegisterResponse.SUCCESS)
                    } else {
                        registerResponse.invoke(RegisterResponse.USERNAME_EXISTS)
                    }
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                registerResponse.invoke(RegisterResponse.CONNECTION_ERROR)
                call.cancel()
            }
        })
    }
}