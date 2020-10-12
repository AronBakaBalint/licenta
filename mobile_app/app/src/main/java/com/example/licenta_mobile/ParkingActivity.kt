package com.example.licenta_mobile

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.licenta_mobile.dialog.ExistingReservationDialog
import com.example.licenta_mobile.dialog.NotEnoughMoneyDialog
import com.example.licenta_mobile.dialog.ReservationDialog
import com.example.licenta_mobile.dialog.ReservationInfoDialog
import com.example.licenta_mobile.dto.MessageDto
import com.example.licenta_mobile.dto.ParkingPlaceDto
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
    private var reservationService: ReservationService? = null
    private var reservationDialog: ReservationDialog? = null
    private var notificationHandler: NotificationHandler? = null
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
        setContentView(R.layout.activity_parking_place_selector)
        reservationService = client!!.create(ReservationService::class.java)
        notificationHandler = NotificationHandler(this)
        startAutoRefresh()
    }

    private fun displayParkingPlaceStatus() {
        val call = reservationService!!.getAllParkingPlaces("Bearer " + Token.jwtToken)
        call.enqueue(object : Callback<List<ParkingPlaceDto>> {
            override fun onResponse(call: Call<List<ParkingPlaceDto>>, response: Response<List<ParkingPlaceDto>>) {
                if (response.isSuccessful) {
                    val parkingPlaces = response.body()!!
                    setParkingPlaceColors(parkingPlaces)
                }
            }

            override fun onFailure(call: Call<List<ParkingPlaceDto>>, t: Throwable) {
                println(t.message)
                call.cancel()
            }
        })
    }

    private fun startAutoRefresh() {
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                displayParkingPlaceStatus()
            }
        }, 0, 1000) //put here time 1000 milliseconds=1 second
    }

    private fun setParkingPlaceColors(parkingPlaces: List<ParkingPlaceDto>) {
        val uiParkingPlaces = getAllParkingPlacesFromUI()
        for (i in uiParkingPlaces.indices) {
            uiParkingPlaces[i].setBackgroundColor(parkingPlaces[i].color)
        }
    }

    private fun getAllParkingPlacesFromUI(): List<Button> {
        val parkingPlaces: MutableList<Button> = ArrayList()
        var layout = findViewById<LinearLayout>(R.id.row1)
        buildRow(layout, 0, parkingPlaces)
        layout = findViewById(R.id.row2)
        buildRow(layout, 1, parkingPlaces)
        layout = findViewById(R.id.row3)
        buildRow(layout, 2, parkingPlaces)
        layout = findViewById(R.id.row4)
        buildRow(layout, 3, parkingPlaces)
        return parkingPlaces
    }

    fun startReservation(view: View) {
        val parkingPlaceId = view.id
        val reservationCost = reservationCost
        val parkingPlaceName = (view as Button).text.toString()
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Reservation")
        builder.setMessage("Reserve parking place $parkingPlaceName?\nInital cost is $reservationCost LEI but you will get it back as you arrive to the parking lot.")
        builder.setPositiveButton("YES") { dialog, which ->
            if (currentSold!! > reservationCost) {
                showReservationDialog(parkingPlaceId)
            } else {
                NotEnoughMoneyDialog.show(this@ParkingActivity)
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("NO") { dialog, which -> dialog.dismiss() }
        val alert = builder.create()
        alert.show()
    }

    private fun showReservationDialog(parkingPlaceId: Int) {
        reservationDialog = ReservationDialog(this, parkingPlaceId)
        reservationDialog!!.show()
    }

    private val reservationCost: Double
        private get() {
            var reservationCost = -1.0
            val call = reservationService!!.getReservationCost("Bearer " + Token.jwtToken)
            try {
                val response = call!!.execute()
                reservationCost = response.body()!!.message.toDouble()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return reservationCost
        }

    fun confirmReservation(view: View?) {
        val introducedLicensePlate = reservationDialog!!.introducedLicensePlate!!.text.toString()
        val parkingPlaceId = reservationDialog!!.parkingPlaceId
        val reservationDto = ReservationDto()
        reservationDto.parkingPlaceId = parkingPlaceId
        reservationDto.userId = userId!!
        reservationDto.licensePlate = introducedLicensePlate
        reservationDialog!!.dismiss()
        val call = reservationService!!.reserveParkingPlace("Bearer " + Token.jwtToken, reservationDto)
        call.enqueue(object : Callback<MessageDto> {
            override fun onResponse(call: Call<MessageDto>, response: Response<MessageDto>) {
                if (response.isSuccessful) {
                    val reservationId = response.body()!!.message.toInt()
                    if (reservationId == -1) {
                        ExistingReservationDialog.show(this@ParkingActivity)
                    } else {
                        ReservationInfoDialog.show(this@ParkingActivity)
                        notificationHandler!!.startCountdownForNotification(reservationId)
                        update()
                    }
                }
            }

            override fun onFailure(call: Call<MessageDto>, t: Throwable) {
                println(t.message)
                call.cancel()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        val menuItem = menu.findItem(R.id.username_item)
        menuItem.title = userName
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.profile) {
            viewProfile()
            return true
        } else if (id == R.id.reservations) {
            viewReservations()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun viewProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun viewReservations() {
        val userId = userId!!
        val call: Call<MutableList<ReservationDto>> = reservationService!!.getAllReservedPlaces("Bearer " + Token.jwtToken, userId)
        call.enqueue(object : Callback<MutableList<ReservationDto>> {
            override fun onResponse(call: Call<MutableList<ReservationDto>>, response: Response<MutableList<ReservationDto>>) {
                if (response.isSuccessful) {
                    val intent = Intent(this@ParkingActivity, ActiveReservationsActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<MutableList<ReservationDto>>, t: Throwable) {
                println(t.message)
                call.cancel()
            }
        })
    }

    private fun buildRow(layout: LinearLayout, row: Int, parkingPlaces: MutableList<Button>) {
        for (i in 0 until layout.childCount) {
            val v = layout.getChildAt(i)
            if (v is Button) {
                v.setId(row * layout.childCount + i + 1)
                parkingPlaces.add(v)
            }
        }
    }
}