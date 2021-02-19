package com.example.licenta_mobile.activity.main.reservations

import android.widget.Button
import android.widget.Toast
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

    private var _toggleFilter = MutableLiveData<Boolean>()
    var activateFilter: LiveData<Boolean> = _toggleFilter

    private var _toastMsg = MutableLiveData<String>()
    var toastMsg: LiveData<String> = _toastMsg

    private val reservationService = RestClient.client!!.create(ReservationService::class.java)

    init {
        loadReservationHistory()
    }

    fun toggleFilter() {
        _toggleFilter.value = true
    }

    private fun loadReservationHistory() {
        val userId = UserData.userId
        val call = reservationService.getReservationHistory("Bearer " + Token.jwtToken, userId)
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

    fun cancelReservation(reservationDto: ReservationDto) {
        val call = reservationService.cancelReservation("Bearer " + Token.jwtToken, reservationDto.id)
        call.enqueue(object : Callback<Void?> {
            override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                if (response.isSuccessful) {
                    _toastMsg.value = "Reservation cancelled"
                    loadReservationHistory()
                }
            }

            override fun onFailure(call: Call<Void?>, t: Throwable) {
                println(t.message)
                call.cancel()
            }
        })
    }

    private fun updateReservationsList(reservationsList: MutableList<ReservationDto>?){
        _reservations.value = reservationsList
    }
}