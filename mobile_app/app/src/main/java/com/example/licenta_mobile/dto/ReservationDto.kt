package com.example.licenta_mobile.dto

import com.example.licenta_mobile.model.SimpleDate

class ReservationDto {
    lateinit var id: Integer
    lateinit var parkingPlaceId: Integer
    lateinit var licensePlate: String
    lateinit var userId: Integer
    lateinit var status: String
    lateinit var startTime: SimpleDate
    lateinit var duration: List<Int>
}