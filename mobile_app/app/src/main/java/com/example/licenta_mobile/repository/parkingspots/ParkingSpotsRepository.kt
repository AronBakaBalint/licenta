package com.example.licenta_mobile.repository.parkingspots

import com.example.licenta_mobile.dto.ParkingSpotDto
import com.example.licenta_mobile.dto.ReservationDto
import java.time.LocalDate

interface ParkingSpotsRepository {

    fun getParkingSpotsState(parkingSpotsResponse: (parkingSpots: List<ParkingSpotDto>) -> Unit)

    fun getReservationSchedule(parkingSpotId: Int, date: LocalDate, reservationScheduleResponse: (reservations: List<ReservationDto>) -> Unit)
}