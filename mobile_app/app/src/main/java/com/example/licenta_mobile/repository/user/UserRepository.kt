package com.example.licenta_mobile.repository.user

import com.example.licenta_mobile.activity.main.reservations.LoginResponse
import com.example.licenta_mobile.dto.LoginRequestDto
import com.example.licenta_mobile.dto.ReservationDto

interface UserRepository {

    fun login(loginRequestDto: LoginRequestDto, loginResponse: (response: LoginResponse) -> Unit)
}