package com.example.licenta_mobile.activity.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.licenta_mobile.R
import com.example.licenta_mobile.dialog.ExistingReservationDialog
import com.example.licenta_mobile.dialog.NotEnoughMoneyDialog
import com.example.licenta_mobile.dialog.ReservationDialog
import com.example.licenta_mobile.dialog.ReservationInfoDialog
import com.example.licenta_mobile.dto.ReservationDto
import com.example.licenta_mobile.model.UserData.currentSold
import com.example.licenta_mobile.model.UserData.update
import com.example.licenta_mobile.model.UserData.userId
import com.example.licenta_mobile.model.UserData.userName
import com.example.licenta_mobile.rest.ReservationService
import com.example.licenta_mobile.rest.RestClient.client
import com.example.licenta_mobile.security.Token
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ParkingActivity : AppCompatActivity() {

    private lateinit var reservationDialog: ReservationDialog
    private val reservationService = client!!.create(ReservationService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onInit()
    }

    public override fun onRestart() {
        super.onRestart()
        onInit()
    }

    private fun onInit() {
        update()
        setContentView(R.layout.fragment_parking_lot)
        startAutoRefresh()
    }

    private fun startAutoRefresh() {
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {

            }
        }, 0, 1000) //put here time 1000 milliseconds=1 second
    }

    fun startReservation(view: View) {
        val parkingPlaceId = view.id
        val pricePerHour = getPriceperHour()
        if (currentSold > pricePerHour) {
            showReservationDialog(parkingPlaceId, pricePerHour)
        } else {
            NotEnoughMoneyDialog.show(this@ParkingActivity)
        }
    }

    private fun showReservationDialog(parkingPlaceId: Int, pricePerHour: Double ) {
        reservationDialog = ReservationDialog(this, parkingPlaceId, pricePerHour)
        reservationDialog.show()
    }

    private fun getPriceperHour(): Double {
        var reservationCost = -1.0
        val call = reservationService.getPricePerHour("Bearer " + Token.jwtToken)
        try {
            val response = call.execute()
            reservationCost = response.body()!!
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return reservationCost
    }

    fun confirmReservation(view: View?) {
        val introducedLicensePlate = reservationDialog.introducedLicensePlate.text.toString()
        val parkingPlaceId = reservationDialog.parkingPlaceId
        val selectedDate = reservationDialog.selectedDate
        val reservationDto = ReservationDto()
        reservationDto.duration = reservationDialog.getIntSelectedHours()
        reservationDto.parkingSpotId = parkingPlaceId
        reservationDto.userId = userId
        reservationDto.licensePlate = introducedLicensePlate
        reservationDto.startTime = selectedDate
        reservationDialog.dismiss()
        val call = reservationService.reserveParkingSpot("Bearer " + Token.jwtToken, reservationDto)
        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val reservationId = response.body()!!
                    if (reservationId == -1) {
                        ExistingReservationDialog.show(this@ParkingActivity)
                    } else {
                        ReservationInfoDialog.show(this@ParkingActivity)
                        update()
                    }
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                println(t.message)
                call.cancel()
            }
        })
    }

    private fun getReservationStartHour(hoursList: List<Int>): Int {
        return hoursList.minOrNull()!!
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        val menuItem = menu.findItem(R.id.username_item)
        menuItem.title = userName
        return true
    }

    private fun viewProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

}