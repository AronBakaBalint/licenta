package com.example.licenta_mobile.dto

import com.example.licenta_mobile.model.SimpleDate

class ReservationDto {
    var id: Int = 0
    var parkingPlaceId: Int = 0
    lateinit var licensePlate: String
    var userId: Int = 0
    lateinit var status: String
    lateinit var startTime: SimpleDate
    lateinit var duration: List<Int>
}