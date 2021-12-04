package com.example.licenta_mobile.dto

import java.time.LocalDate

class ReservationDto {
    var id: Int = 0
    var parkingSpotId: Int = 0
    lateinit var licensePlate: String
    lateinit var status: String
    lateinit var startTime: String
    lateinit var duration: List<Int>
}