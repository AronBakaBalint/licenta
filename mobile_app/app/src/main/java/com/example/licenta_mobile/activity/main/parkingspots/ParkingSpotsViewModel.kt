package com.example.licenta_mobile.activity.main.parkingspots

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.licenta_mobile.dto.ParkingSpotDto
import com.example.licenta_mobile.repository.parkingspots.ParkingSpotsRepository
import com.example.licenta_mobile.repository.parkingspots.ParkingSpotsRepositoryImpl

class ParkingSpotsViewModel : ViewModel() {

    var spotsState: List<MutableLiveData<ParkingSpotDto>> = getInitialSpotsState()

    private val parkingSpotsRepository: ParkingSpotsRepository = ParkingSpotsRepositoryImpl()

    private val _reservedSpotId = MutableLiveData<Int>()
    val reservedSpotId: LiveData<Int> = _reservedSpotId

    init {
        getParkingSpotsState()
    }

    fun reserve(pos: Int){
        val spotId = spotsState[pos].value?.id!!
        _reservedSpotId.value = spotId
    }

    private fun getInitialSpotsState(): List<MutableLiveData<ParkingSpotDto>> {
        val defaultColors = mutableListOf<MutableLiveData<ParkingSpotDto>>()
        for(i in 0..23){
            defaultColors.add(MutableLiveData(ParkingSpotDto(i, 546454657, "reserved", "")))
        }
        return defaultColors
    }

    private fun getParkingSpotsState() {
        parkingSpotsRepository.getParkingSpotsState {
            updateParkingSpotsState(it)
        }
    }

    private fun updateParkingSpotsState(newState: List<ParkingSpotDto>)  {
        for(i in newState.indices){
            spotsState[i].value = ParkingSpotDto(newState[i].id, newState[i].color, newState[i].status, newState[i].occupierCarPlate)
        }
    }

}