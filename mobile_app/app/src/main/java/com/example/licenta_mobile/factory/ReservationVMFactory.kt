package com.example.licenta_mobile.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.licenta_mobile.activity.reservation.SpotReservationViewModel

class ReservationVMFactory(private val spotId: Int?) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SpotReservationViewModel(spotId) as T
    }
}