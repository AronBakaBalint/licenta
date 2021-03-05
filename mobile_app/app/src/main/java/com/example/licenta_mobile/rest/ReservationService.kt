package com.example.licenta_mobile.rest

import com.example.licenta_mobile.dto.ParkingSpotDto
import com.example.licenta_mobile.dto.ReservationDto
import com.example.licenta_mobile.model.SimpleDate
import retrofit2.Call
import retrofit2.http.*

interface ReservationService {

    @GET("/parking")
    fun getAllParkingPlaces(): Call<List<ParkingSpotDto>>

    @POST("/reservation")
    fun reserveParkingSpot(@Body reservationDto: ReservationDto): Call<Int>

    @DELETE("/reservation/{id}")
    fun cancelReservation(@Path("id") id: Int): Call<Void>

    @GET("/parking/reserved")
    fun getReservationHistory(): Call<MutableList<ReservationDto>>

    @GET("/reservation/extension")
    fun getExtensionCost(): Call<Double>

    @GET("/reservation")
    fun getPricePerHour(): Call<Double>

    @POST("/reservation/date/{id}")
    fun getReservationSchedule(@Path("id") spotId: Int, @Body reservationDate: SimpleDate): Call<List<ReservationDto>>

    @PUT("/reservation/{id}")
    fun extendReservation(@Path("id") id: Int): Call<Void>
}