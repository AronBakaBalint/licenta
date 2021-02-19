package com.example.licenta_mobile.rest

import androidx.databinding.ObservableList
import com.example.licenta_mobile.dto.ParkingSpotDto
import com.example.licenta_mobile.dto.ReservationDto
import com.example.licenta_mobile.model.SimpleDate
import retrofit2.Call
import retrofit2.http.*

interface ReservationService {

    @GET("/parking")
    @Headers("Content-Type: application/json")
    fun getAllParkingPlaces(@Header("Authorization") auth: String): Call<List<ParkingSpotDto>>

    @POST("/reservation")
    @Headers("Content-Type: application/json")
    fun reserveParkingSpot(@Header("Authorization") auth: String, @Body reservationDto: ReservationDto): Call<Int>

    @GET("/reservation/{id}")
    @Headers("Content-Type: application/json")
    fun isReservationPending(@Header("Authorization") auth: String, @Path("id") id: Int): Call<Boolean>

    @DELETE("/reservation/{id}")
    @Headers("Content-Type: application/json")
    fun cancelReservation(@Header("Authorization") auth: String, @Path("id") id: Int): Call<Void>

    @GET("/parking/unoccupied/{id}")
    @Headers("Content-Type: application/json")
    fun getUnoccupiedPlaces(@Header("Authorization") auth: String, @Path("id") id: Int): Call<MutableList<ReservationDto>>

    @GET("/parking/reserved/{id}")
    @Headers("Content-Type: application/json")
    fun getReservationHistory(@Header("Authorization") auth: String, @Path("id") id: Int): Call<MutableList<ReservationDto>>

    @GET("/reservation/extension")
    @Headers("Content-Type: application/json")
    fun getExtensionCost(@Header("Authorization") auth: String): Call<Double>

    @GET("/reservation")
    @Headers("Content-Type: application/json")
    fun getPricePerHour(@Header("Authorization") auth: String): Call<Double>

    @POST("/reservation/date/{id}")
    @Headers("Content-Type: application/json")
    fun getAllActiveReservations(@Header("Authorization") auth: String, @Path("id") id: Int, @Body reservationDate: SimpleDate): Call<List<ReservationDto>>

    @PUT("/reservation/{id}")
    @Headers("Content-Type: application/json")
    fun extendReservation(@Header("Authorization") auth: String, @Path("id") id: Int): Call<Void>
}