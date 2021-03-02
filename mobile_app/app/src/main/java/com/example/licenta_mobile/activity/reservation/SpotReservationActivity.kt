package com.example.licenta_mobile.activity.reservation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.licenta_mobile.R
import com.example.licenta_mobile.base.BaseActivity

class SpotReservationActivity : BaseActivity(), ReservationCommandListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spot_reservation)
        createFragment()
    }

    private fun createFragment() {
        addFragment(R.id.container, ReservationDateFragment.newInstance())
    }

    override fun cancelReservation() {
        finish()
    }

    override fun navigateToHourPicker() {
        addFragment(R.id.container, ReservationHoursFragment.newInstance())
    }

    override fun navigateToLicensePlate() {
        addFragment(R.id.container, ReservationLicensePlateFragment.newInstance())
    }

    override fun navigateToSummary() {
        addFragment(R.id.container, ReservationSummaryFragment.newInstance())
    }
}