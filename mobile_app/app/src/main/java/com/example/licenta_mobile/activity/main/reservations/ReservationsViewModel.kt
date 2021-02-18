package com.example.licenta_mobile.activity.main.reservations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.licenta_mobile.dto.ReservationDto
import com.example.licenta_mobile.model.UserData
import com.example.licenta_mobile.rest.ReservationService
import com.example.licenta_mobile.rest.RestClient
import com.example.licenta_mobile.security.Token
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReservationsViewModel : ViewModel() {

    private var _reservations = MutableLiveData<List<ReservationDto>>()
    var reservations: LiveData<List<ReservationDto>> = _reservations

    private val reservationService = RestClient.client!!.create(ReservationService::class.java)

    init {
        loadReservationHistory()
    }

    private fun loadReservationHistory() {
        val userId = UserData.userId
        val call = reservationService.getUnoccupiedPlaces("Bearer " + Token.jwtToken, userId)
        call.enqueue(object : Callback<MutableList<ReservationDto>> {
            override fun onResponse(call: Call<MutableList<ReservationDto>>, response: Response<MutableList<ReservationDto>>) {
                if (response.isSuccessful) {
                    updateReservationsList(response.body())
                }
            }

            override fun onFailure(call: Call<MutableList<ReservationDto>>, t: Throwable) {
                println(t.message)
                call.cancel()
            }
        })
    }

    private fun updateReservationsList(reservationsList: MutableList<ReservationDto>?){
        _reservations.value = reservationsList!!
    }
}