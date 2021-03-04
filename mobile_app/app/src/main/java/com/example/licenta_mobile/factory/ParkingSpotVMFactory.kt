package com.example.licenta_mobile.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.licenta_mobile.activity.main.parkingspots.ParkingSpotsViewModel


class ParkingSpotVMFactory(private val reservation: (Int) -> Unit) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ParkingSpotsViewModel(reservation) as T
    }
}