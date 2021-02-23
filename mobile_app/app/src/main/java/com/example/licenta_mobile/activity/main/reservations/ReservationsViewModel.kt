package com.example.licenta_mobile.activity.main.reservations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.licenta_mobile.dto.ReservationDto
import com.example.licenta_mobile.repository.reservations.ReservationsRepository
import com.example.licenta_mobile.repository.reservations.ReservationsRepositoryImpl

class ReservationsViewModel : ViewModel() {

    private var _reservations = MutableLiveData<List<ReservationDto>>()
    var reservations: LiveData<List<ReservationDto>> = _reservations

    private var _toggleFilter = MutableLiveData<Boolean>()
    var activateFilter: LiveData<Boolean> = _toggleFilter

    private var _toastMsg = MutableLiveData<String>()
    var toastMsg: LiveData<String> = _toastMsg

    private val reservationRepository: ReservationsRepository = ReservationsRepositoryImpl()

    init {
        loadReservationHistory()
    }

    fun toggleFilter() {
        _toggleFilter.value = true
    }

    private fun loadReservationHistory() {
        reservationRepository.loadReservationHistory {
            updateReservationsList(it)
        }
    }

    fun cancelReservation(reservationDto: ReservationDto) {
        reservationRepository.cancelReservation(reservationDto.id) { response ->
            if (response) {
                _toastMsg.value = "Reservation cancelled"
                loadReservationHistory()
            }
        }
    }

    private fun updateReservationsList(reservationsList: List<ReservationDto>?) {
        _reservations.value = reservationsList
    }
}