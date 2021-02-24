package com.example.licenta_mobile.dialog

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import com.example.licenta_mobile.R
import com.example.licenta_mobile.adapter.ReservationSetupAdapter
import com.example.licenta_mobile.lambda.PriceUpdater
import com.example.licenta_mobile.model.SimpleDate
import com.example.licenta_mobile.model.UserData
import com.example.licenta_mobile.model.UserData.currentSold
import com.example.licenta_mobile.rest.ReservationService
import com.example.licenta_mobile.rest.RestClient.client
import com.example.licenta_mobile.security.Token
import java.util.*
import kotlin.collections.ArrayList

class ReservationDialog(private val activity: Activity, var parkingPlaceId: Int, private val pricePerHour: Double) : Dialog(activity) {

    lateinit var introducedLicensePlate: EditText
    lateinit var selectedDate: SimpleDate
    private lateinit var listView: ListView
    private lateinit var selectedHours: MutableList<Button>
    private lateinit var confirmButton: Button
    private val service: ReservationService = client!!.create(ReservationService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.reservation_dialog)
        listView = findViewById(R.id.hourList)
        window!!.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        confirmButton = findViewById(R.id.button3)
        confirmButton.isClickable = false
        introducedLicensePlate = findViewById(R.id.licensePlateEditor)
        introducedLicensePlate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                confirmButton.isClickable = introducedLicensePlate.text.isNotBlank()
            }
        })
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
        val occupiedHours = getReservationSchedule(parkingPlaceId, selectedDate)
        val priceUpdater: () -> Unit = {
            val displayedPrice = findViewById<TextView>(R.id.amountToPay)
            val priceToPay = pricePerHour * selectedHours.size
            if(priceToPay > currentSold){
                displayedPrice.setTextColor(Color.RED)
                confirmButton.isClickable = false
            } else {
                displayedPrice.setTextColor(Color.rgb(0, 175, 32))
                confirmButton.isClickable = true
            }
            displayedPrice.text = "$priceToPay LEI"
        }
        listView.adapter = ReservationSetupAdapter(get24HoursList(), occupiedHours, selectedHours as ArrayList<Button>, activity, priceUpdater)
        listView.setSelection(12)
    }

    private fun getReservationSchedule(parkingPlaceId: Int, reservationDate: SimpleDate): List<Int> {
        val reservationList: MutableList<Int> = ArrayList()
        val callSync = service.getAllActiveReservations(parkingPlaceId, reservationDate)
        try {
            val response = callSync.execute()
            val apiResponse = response.body()!!
            for (reservation in apiResponse) {
                for (d in reservation.duration) {
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

    fun getIntSelectedHours(): MutableList<Int> {
        val intHoursList: MutableList<Int> = ArrayList()
        for (b: Button in selectedHours) {
            intHoursList.add(b.text.toString().replace(":00", "").toInt())
        }
        return intHoursList
    }
}