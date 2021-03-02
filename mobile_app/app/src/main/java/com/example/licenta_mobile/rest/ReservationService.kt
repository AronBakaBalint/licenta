package com.example.licenta_mobile.rest

import androidx.databinding.ObservableList
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

    @GET("/reservation/{id}")
    fun isReservationPending(@Path("id") id: Int): Call<Boolean>

    @DELETE("/reservation/{id}")
    fun cancelReservation(@Path("id") id: Int): Call<Void>

    @GET("/parking/reserved/{id}")
    fun getReservationHistory(@Path("id") id: Int): Call<MutableList<ReservationDto>>

    @GET("/reservation/extension")
    fun getExtensionCost(): Call<Double>

    @GET("/reservation")
    fun getPricePerHour(): Call<Double>

    @POST("/reservation/date/{id}")
    fun getReservationSchedule(@Path("id") id: Int, @Body reservationDate: SimpleDate): Call<List<ReservationDto>>

    @PUT("/reservation/{id}")
    fun extendReservation(@Path("id") id: Int): Call<Void>
}