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

    var colors: List<MutableLiveData<Int>> = getDefaultColors()

    init {
        getParkingSpotsState()
    }

    fun reserve(spotId: Int){
        println("Reserved")
    }

    private fun getDefaultColors(): List<MutableLiveData<Int>> {
        val defaultColors = mutableListOf<MutableLiveData<Int>>()
        repeat(24){
            defaultColors.add(MutableLiveData(546454657))
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
            colors[i].value = newState[i].color
            println("UPDATED")
        }
    }

}