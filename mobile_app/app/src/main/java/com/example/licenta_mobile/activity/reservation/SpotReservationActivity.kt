package com.example.licenta_mobile.activity.reservation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.example.licenta_mobile.R

class SpotReservationActivity : AppCompatActivity(), ReservationCommandListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spot_reservation)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ReservationDateFragment.newInstance())
                    .commitNow()
        }
    }

    override fun cancelReservation() {
        finish()
    }

    override fun navigateToHourPicker() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, ReservationHoursFragment.newInstance())
                .commitNow()
    }

    override fun navigateToLicensePlate() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, ReservationLicensePlateFragment.newInstance())
                .commitNow()
    }

    override fun navigateToSummary() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, ReservationSummaryFragment.newInstance())
                .commitNow()
    }
}