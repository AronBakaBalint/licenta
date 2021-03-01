package com.example.licenta_mobile.activity.reservation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SpotReservationViewModel : ViewModel() {

    private val _cancelReservation = MutableLiveData<Boolean>()
    val cancelReservation: LiveData<Boolean> = _cancelReservation

    private val _navigateToHourPicker = MutableLiveData<Boolean>()
    val navigateToHourPicker: LiveData<Boolean> = _navigateToHourPicker

    private val _navigateToLicensePlate = MutableLiveData<Boolean>()
    val navigateToLicensePlate: LiveData<Boolean> = _navigateToLicensePlate

    private val _navigateToSummary = MutableLiveData<Boolean>()
    val navigateToSummary: LiveData<Boolean> = _navigateToSummary

    fun navigateToHourPicker() {
        _navigateToHourPicker.value = true
    }

    fun navigateToLicensePlate() {
        _navigateToLicensePlate.value = true
    }

    fun navigateToSummary() {
        _navigateToSummary.value = true
    }

    fun cancelReservation() {
        _cancelReservation.value = true
    }
}