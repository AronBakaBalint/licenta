package com.example.licenta_mobile.rest

import com.example.licenta_mobile.dto.MessageDto
import com.example.licenta_mobile.dto.ParkingPlaceDto
import com.example.licenta_mobile.dto.ReservationDto
import com.example.licenta_mobile.model.SimpleDate
import retrofit2.Call
import retrofit2.http.*

interface ReservationService {

    @GET("/parking")
    @Headers("Content-Type: application/json")
    fun getAllParkingPlaces(@Header("Authorization") auth: String): Call<List<ParkingPlaceDto>>

    @POST("/reservation")
    @Headers("Content-Type: application/json")
    fun reserveParkingSpot(@Header("Authorization") auth: String, @Body reservationDto: ReservationDto): Call<MessageDto>

    @GET("/reservation/{id}")
    @Headers("Content-Type: application/json")
    fun getReservationStatus(@Header("Authorization") auth: String, @Path("id") id: Int): Call<MessageDto>

    @DELETE("/reservation/{id}")
    @Headers("Content-Type: application/json")
    fun cancelReservation(@Header("Authorization") auth: String, @Path("id") id: Int): Call<Void>

    @GET("/parking/unoccupied/{id}")
    @Headers("Content-Type: application/json")
    fun getUnoccupiedPlaces(@Header("Authorization") auth: String, @Path("id") id: Int): Call<MutableList<ReservationDto>>

    @GET("/parking/reserved/{id}")
    @Headers("Content-Type: application/json")
    fun getAllReservedPlaces(@Header("Authorization") auth: String, @Path("id") id: Int): Call<MutableList<ReservationDto>>

    @GET("/reservation/extension")
    @Headers("Content-Type: application/json")
    fun getExtensionCost(@Header("Authorization") auth: String): Call<MessageDto>

    @GET("/reservation")
    @Headers("Content-Type: application/json")
    fun getReservationCost(@Header("Authorization") auth: String): Call<MessageDto>

    @POST("/reservation/date/{id}")
    @Headers("Content-Type: application/json")
    fun getAllActiveReservations(@Header("Authorization") auth: String, @Path("id") id: Int, @Body reservationDate: SimpleDate): Call<List<ReservationDto>>

    @PUT("/reservation/{id}")
    @Headers("Content-Type: application/json")
    fun extendReservation(@Header("Authorization") auth: String, @Path("id") id: Int): Call<Void>
}