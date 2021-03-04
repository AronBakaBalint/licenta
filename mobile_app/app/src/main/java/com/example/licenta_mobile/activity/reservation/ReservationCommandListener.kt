package com.example.licenta_mobile.activity.reservation

interface ReservationCommandListener {

    fun cancelReservation()

    fun navigateToHourPicker()

    fun navigateToLicensePlate()

    fun navigateToSummary()

    fun navigateToMainActivity()
}