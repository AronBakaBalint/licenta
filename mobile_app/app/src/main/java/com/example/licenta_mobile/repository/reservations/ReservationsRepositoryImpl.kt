package com.example.licenta_mobile.repository.reservations

import com.example.licenta_mobile.dto.ReservationDto
import com.example.licenta_mobile.rest.ReservationService
import com.example.licenta_mobile.rest.RestClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReservationsRepositoryImpl : ReservationsRepository {

    private val reservationService = RestClient.client?.create(ReservationService::class.java)

    override fun loadReservationHistory(reservationHistoryResponse: (reservations: List<ReservationDto>) -> Unit) {
        val call = reservationService?.getReservationHistory()
        call?.enqueue(object : Callback<MutableList<ReservationDto>> {
            override fun onResponse(call: Call<MutableList<ReservationDto>>, response: Response<MutableList<ReservationDto>>) {
                if (response.isSuccessful) {
                    reservationHistoryResponse.invoke(response.body()!!)
                }
            }

            override fun onFailure(call: Call<MutableList<ReservationDto>>, t: Throwable) {
                println(t.message)
                call.cancel()
            }
        })
    }

    override fun cancelReservation(reservationId: Int, reservationCancelResponse: (response: Boolean) -> Unit) {
        val call = reservationService?.cancelReservation(reservationId)
        call?.enqueue(object : Callback<Void?> {
            override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                if (response.isSuccessful) {
                    reservationCancelResponse.invoke(true)
                }
            }

            override fun onFailure(call: Call<Void?>, t: Throwable) {
                println(t.message)
                call.cancel()
            }
        })
    }

    override fun makeReservation(reservationDto: ReservationDto, reservationResponse: (response: Int) -> Unit) {
        val call = reservationService?.reserveParkingSpot(reservationDto)
        call?.enqueue(object : Callback<Int?> {
            override fun onResponse(call: Call<Int?>, response: Response<Int?>) {
                if (response.isSuccessful) {
                    reservationResponse.invoke(response.body()!!)
                }
            }

            override fun onFailure(call: Call<Int?>, t: Throwable) {
                println(t.message)
                call.cancel()
            }
        })
    }
}