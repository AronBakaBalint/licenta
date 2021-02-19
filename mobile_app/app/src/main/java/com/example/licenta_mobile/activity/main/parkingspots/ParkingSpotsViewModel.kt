package com.example.licenta_mobile.activity.main.parkingspots

import android.graphics.Color
import android.widget.Button
import android.widget.LinearLayout
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.licenta_mobile.R
import com.example.licenta_mobile.dto.ParkingSpotDto
import com.example.licenta_mobile.model.UserData
import com.example.licenta_mobile.rest.LoginService
import com.example.licenta_mobile.rest.ReservationService
import com.example.licenta_mobile.rest.RestClient
import com.example.licenta_mobile.security.Token
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class ParkingSpotsViewModel : ViewModel() {

    private val parkingService = RestClient.client!!.create(ReservationService::class.java)

    var spotsState: List<MutableLiveData<ParkingSpotDto>> = getInitialSpotsState()

    init {
        getParkingSpotsState()
    }

    fun reserve(pos: Int){
        val spotId = spotsState[pos].value?.id
        println("$spotId Reserved")
    }

    private fun getInitialSpotsState(): List<MutableLiveData<ParkingSpotDto>> {
        val defaultColors = mutableListOf<MutableLiveData<ParkingSpotDto>>()
        for(i in 0..23){
            defaultColors.add(MutableLiveData(ParkingSpotDto(i, 546454657, "reserved", "")))
        }
        return defaultColors
    }

    private fun getParkingSpotsState(){
        val call = parkingService.getAllParkingPlaces("Bearer ${Token.jwtToken}")
        call.enqueue(object : Callback<List<ParkingSpotDto>> {
            override fun onResponse(call: Call<List<ParkingSpotDto>>, response: Response<List<ParkingSpotDto>>) {
                if (response.isSuccessful) {
                    val parkingSpotsState = response.body()!!
                    updateParkingSpotsState(parkingSpotsState)
                }
            }

            override fun onFailure(call: Call<List<ParkingSpotDto>>, t: Throwable) {
                t.printStackTrace()
                call.cancel()
            }
        })
    }

    private fun updateParkingSpotsState(newState: List<ParkingSpotDto>)  {
        for(i in newState.indices){
            spotsState[i].value = ParkingSpotDto(newState[i].id, newState[i].color, newState[i].status, newState[i].occupierCarPlate)
        }
    }

    fun isFree(pos: Int): Boolean {
        return spotsState[pos].value?.status == "free"
    }

}