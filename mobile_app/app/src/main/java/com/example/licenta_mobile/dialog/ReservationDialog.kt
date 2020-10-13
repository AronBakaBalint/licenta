package com.example.licenta_mobile.dialog

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ListView
import com.example.licenta_mobile.R
import com.example.licenta_mobile.adapter.ReservationSetupAdapter
import com.example.licenta_mobile.model.SimpleDate
import com.example.licenta_mobile.rest.ReservationService
import com.example.licenta_mobile.rest.RestClient.client
import com.example.licenta_mobile.security.Token
import java.util.*

class ReservationDialog(private val activity: Activity, var parkingPlaceId: Int) : Dialog(activity) {

    var introducedLicensePlate: EditText? = null
    private var selectedDate: SimpleDate? = null
    private var service: ReservationService? = null
    private var listView: ListView? = null
    private var selectedHours : MutableList<Button>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.reservation_dialog)
        listView = findViewById(R.id.hourList)
        window!!.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        introducedLicensePlate = findViewById(R.id.licensePlateEditor)
        service = client!!.create(ReservationService::class.java)
        val datePicker = findViewById<View>(R.id.datePicker1) as DatePicker
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        datePicker.init(calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH]) { _, year, month, dayOfMonth -> refreshTimeTable(year, month, dayOfMonth) }
        val date = Date()
        val cal = Calendar.getInstance()
        cal.time = date
        val year = cal[Calendar.YEAR]
        val month = cal[Calendar.MONTH]
        val day = cal[Calendar.DAY_OF_MONTH]
        refreshTimeTable(year, month, day)
    }

    private fun refreshTimeTable(year: Int, month: Int, day: Int) {
        selectedHours = ArrayList()
        selectedDate = SimpleDate(day, month + 1, year)
        val occupiedHours = getReservationSchedule(parkingPlaceId, selectedDate!!)
        listView!!.adapter = ReservationSetupAdapter(get24HoursList(), occupiedHours, selectedHours as ArrayList<Button>, activity)
        listView!!.setSelection(12)
    }

    private fun getReservationSchedule(parkingPlaceId: Int, reservationDate: SimpleDate): List<Int> {
        val reservationList: MutableList<Int> = ArrayList()
        val callSync = service!!.getAllActiveReservations("Bearer " + Token.jwtToken, parkingPlaceId, reservationDate)
        try {
            val response = callSync!!.execute()
            val apiResponse = response.body()
            for (reservation in apiResponse!!) {
                for (d in reservation!!.duration) {
                    reservationList.add(d)
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return reservationList
    }

    private fun get24HoursList(): List<String> {
        val list: MutableList<String> = ArrayList()
        for (i in 0..23) {
            list.add(if (i < 10) "0$i:00" else "$i:00")
        }
        return list
    }
}