package com.example.licenta_mobile.repository.parkingspots

import com.example.licenta_mobile.dto.ParkingSpotDto
import com.example.licenta_mobile.dto.ReservationDto
import com.example.licenta_mobile.model.SimpleDate
import com.example.licenta_mobile.rest.ReservationService
import com.example.licenta_mobile.rest.RestClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ParkingSpotsRepositoryImpl : ParkingSpotsRepository {

    private val parkingService = RestClient.client!!.create(ReservationService::class.java)

    override fun getParkingSpotsState(parkingSpotsResponse: (parkingSpots: List<ParkingSpotDto>) -> Unit) {
        val call = parkingService.getAllParkingPlaces()
        call.enqueue(object : Callback<List<ParkingSpotDto>> {
            override fun onResponse(call: Call<List<ParkingSpotDto>>, response: Response<List<ParkingSpotDto>>) {
                if (response.isSuccessful) {
                    parkingSpotsResponse.invoke(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<ParkingSpotDto>>, t: Throwable) {
                t.printStackTrace()
                call.cancel()
            }
        })
    }

    override fun getReservationSchedule(parkingSpotId: Int, date: SimpleDate, reservationScheduleResponse: (reservations: List<ReservationDto>) -> Unit) {
        val call = parkingService.getReservationSchedule(parkingSpotId, date)
        call.enqueue(object : Callback<List<ReservationDto>> {
            override fun onResponse(call: Call<List<ReservationDto>>, response: Response<List<ReservationDto>>) {
                if (response.isSuccessful) {
                    reservationScheduleResponse.invoke(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<ReservationDto>>, t: Throwable) {
                t.printStackTrace()
                call.cancel()
            }
        })
    }

}