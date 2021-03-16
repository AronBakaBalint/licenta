package com.example.licenta_mobile.activity.main.reservations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.licenta_mobile.base.BaseViewModel
import com.example.licenta_mobile.di.DaggerAppComponent
import com.example.licenta_mobile.dto.ReservationDto
import com.example.licenta_mobile.repository.reservations.ReservationsRepository
import com.example.licenta_mobile.util.Event
import javax.inject.Inject

class ReservationsViewModel : BaseViewModel() {

    private var _reservations = MutableLiveData<List<ReservationDto>>()
    var reservations: LiveData<List<ReservationDto>> = _reservations

    private var _updateReservationHistory = MediatorLiveData<Boolean>()
    var updateReservationHistory: LiveData<Boolean> = _updateReservationHistory

    private var _toastMsg = MutableLiveData<Event<String>>()
    var toastMsg: LiveData<Event<String>> = _toastMsg

    private var _toggleFilter = MutableLiveData<Boolean>()

    @Inject
    lateinit var reservationRepository: ReservationsRepository

    init {
        DaggerAppComponent.create().inject(this)
        loadReservationHistory()
        _updateReservationHistory.addSource(_toggleFilter) { _updateReservationHistory.value = true }
        _updateReservationHistory.addSource(_reservations) { _updateReservationHistory.value = true }
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
                _toastMsg.value = Event("Reservation cancelled")
                loadReservationHistory()
            }
        }
    }

    private fun updateReservationsList(reservationsList: List<ReservationDto>?) {
        _reservations.value = reservationsList!!
    }
}