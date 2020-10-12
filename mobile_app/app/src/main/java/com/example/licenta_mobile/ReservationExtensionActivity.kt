package com.example.licenta_mobile

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.licenta_mobile.adapter.PendingReservationAdapter
import com.example.licenta_mobile.dto.ReservationDto
import com.example.licenta_mobile.model.UserData.userId
import com.example.licenta_mobile.rest.ReservationService
import com.example.licenta_mobile.rest.RestClient.client
import com.example.licenta_mobile.security.Token
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReservationExtensionActivity : AppCompatActivity() {
    private var reservationService: ReservationService? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservations)
        reservationService = client!!.create(ReservationService::class.java)
        val viewTitle = findViewById<TextView>(R.id.reservationTitle)
        viewTitle.text = getString(R.string.pendingReservations)
        handleUnconfirmedReservations()
    }

    override fun onBackPressed() {
        val intent = Intent(this, ParkingActivity::class.java)
        startActivity(intent)
    }

    private fun handleUnconfirmedReservations() {
        val userId = userId!!
        val call = reservationService!!.getUnoccupiedPlaces("Bearer " + Token.jwtToken, userId)
        call.enqueue(object : Callback<MutableList<ReservationDto>?> {
            override fun onResponse(call: Call<MutableList<ReservationDto>?>, response: Response<MutableList<ReservationDto>?>) {
                if (response.isSuccessful) {
                    val unconfirmedReservations = response.body()
                    buildView(unconfirmedReservations)
                }
            }

            override fun onFailure(call: Call<MutableList<ReservationDto>?>, t: Throwable) {
                println(t.message)
                call.cancel()
            }
        })
    }

    private fun buildView(pendingReservations: MutableList<ReservationDto>?) {
        val notificationHandler = NotificationHandler(this)
        val adapter = PendingReservationAdapter(pendingReservations, notificationHandler, this)
        val lView = findViewById<ListView>(R.id.pendingReservationList)
        lView.adapter = adapter
    }
}