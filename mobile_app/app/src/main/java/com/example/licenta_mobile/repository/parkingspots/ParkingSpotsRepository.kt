package com.example.licenta_mobile.repository.parkingspots

import com.example.licenta_mobile.activity.login.LoginResponse
import com.example.licenta_mobile.dto.ParkingSpotDto

interface ParkingSpotsRepository {

    fun getParkingSpotsState(parkingSpotsResponse: (parkingSpots: List<ParkingSpotDto>) -> Unit)
}