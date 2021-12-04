package com.example.licenta_mobile.rest

import com.example.licenta_mobile.dto.ParkingSpotDto
import com.example.licenta_mobile.dto.ReservationDto
import retrofit2.Call
import retrofit2.http.*
import java.time.LocalDate

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

    @GET("/reservation/schedule")
    fun getReservationSchedule(@Query("id") spotId: Int, @Query("date") reservationDate: String): Call<List<ReservationDto>>

    @PUT("/reservation/{id}")
    fun extendReservation(@Path("id") id: Int): Call<Void>
}