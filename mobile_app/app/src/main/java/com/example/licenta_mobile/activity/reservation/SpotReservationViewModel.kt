package com.example.licenta_mobile.activity.reservation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.licenta_mobile.base.BaseViewModel
import com.example.licenta_mobile.model.SimpleDate
import com.example.licenta_mobile.repository.parkingspots.ParkingSpotsRepositoryImpl
import java.util.*


class SpotReservationViewModel(private val spotId: Int?) : BaseViewModel() {

    private val _cancelReservation = MutableLiveData<Boolean>()
    val cancelReservation: LiveData<Boolean> = _cancelReservation

    private val _navigateToHourPicker = MutableLiveData<Boolean>()
    val navigateToHourPicker: LiveData<Boolean> = _navigateToHourPicker

    private val _navigateToLicensePlate = MutableLiveData<Boolean>()
    val navigateToLicensePlate: LiveData<Boolean> = _navigateToLicensePlate

    private val _navigateToSummary = MutableLiveData<Boolean>()
    val navigateToSummary: LiveData<Boolean> = _navigateToSummary

    private val _reservationHours = MutableLiveData<List<Int>>()
    val reservationHours: LiveData<List<Int>> = _reservationHours

    private var selectedDate = today()

    private val parkingSpotsRepository = ParkingSpotsRepositoryImpl()

    init {
        getParkingSpotSchedule()
    }

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

    fun setSelectedDate(year: Int, month: Int, day: Int) {
        selectedDate = SimpleDate(day, month, year)
    }

    private fun today(): SimpleDate {
        val date = Date()
        val cal = Calendar.getInstance()
        cal.time = date
        val year = cal[Calendar.YEAR]
        val month = cal[Calendar.MONTH]
        val day = cal[Calendar.DAY_OF_MONTH]
        return SimpleDate(day, month + 1, year)
    }

    private fun getParkingSpotSchedule() {
        val reservedHours = arrayListOf<Int>()
        parkingSpotsRepository.getReservationSchedule(spotId!!, selectedDate) { reservations ->
            reservations.forEach { reservation ->
                reservation.duration.forEach { reservedHours.add(it) }
            }
        }
        _reservationHours.value = reservedHours
    }
}