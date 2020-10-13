package com.example.licenta_mobile.dto

import com.example.licenta_mobile.model.SimpleDate

class ReservationDto {
    var id: Int = 0
    var userId: Int = 0
    var parkingSpotId: Int = 0
    lateinit var licensePlate: String
    lateinit var status: String
    lateinit var startTime: SimpleDate
    lateinit var duration: List<Int>
}