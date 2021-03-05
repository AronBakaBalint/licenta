package com.example.licenta_mobile.repository.user

import com.example.licenta_mobile.activity.login.LoginResponse
import com.example.licenta_mobile.activity.register.RegisterResponse
import com.example.licenta_mobile.dto.LoginRequestDto
import com.example.licenta_mobile.dto.UserDataDto

interface UserRepository {

    fun login(loginRequestDto: LoginRequestDto, loginResponse: (response: LoginResponse) -> Unit)

    fun register(fullName: String, username: String, email: String, password: String, registerResponse: (response: RegisterResponse) -> Unit)

    fun addMoney(userId: Int, amount: Double, moneyAddResponse: (response: Boolean) -> Unit)

    fun getUserDetails(userId: Int, userDetails: (response: UserDataDto) -> Unit)
}