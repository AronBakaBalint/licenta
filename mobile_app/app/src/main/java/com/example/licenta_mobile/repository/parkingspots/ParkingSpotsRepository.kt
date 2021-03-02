package com.example.licenta_mobile.repository.parkingspots

import com.example.licenta_mobile.activity.login.LoginResponse
import com.example.licenta_mobile.dto.ParkingSpotDto
import com.example.licenta_mobile.dto.ReservationDto
import com.example.licenta_mobile.model.SimpleDate

interface ParkingSpotsRepository {

    fun getParkingSpotsState(parkingSpotsResponse: (parkingSpots: List<ParkingSpotDto>) -> Unit)

    fun getReservationSchedule(parkingSpotId: Int, date: SimpleDate, reservationScheduleResponse: (reservations: List<ReservationDto>) -> Unit)
}