package com.example.licenta_mobile.activity.reservation

import android.os.Bundle
import com.example.licenta_mobile.R
import com.example.licenta_mobile.activity.main.MainActivity
import com.example.licenta_mobile.base.BaseActivity

class SpotReservationActivity : BaseActivity(), ReservationCommandListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spot_reservation)
        createFragment()
    }

    private fun createFragment() {
        val parkingSpotId = intent.getStringExtra(MainActivity.PARKING_SPOT_ID_EXTRA)?.toInt()
        addFragment(R.id.container, ReservationDateFragment.newInstance(parkingSpotId))
    }

    override fun cancelReservation() {
        finish()
    }

    override fun navigateToHourPicker() {
        val parkingSpotId = intent.getStringExtra(MainActivity.PARKING_SPOT_ID_EXTRA)?.toInt()
        addFragment(R.id.container, ReservationHoursFragment.newInstance(parkingSpotId))
    }

    override fun navigateToLicensePlate() {
        val parkingSpotId = intent.getStringExtra(MainActivity.PARKING_SPOT_ID_EXTRA)?.toInt()
        addFragment(R.id.container, ReservationLicensePlateFragment.newInstance(parkingSpotId))
    }

    override fun navigateToSummary() {
        val parkingSpotId = intent.getStringExtra(MainActivity.PARKING_SPOT_ID_EXTRA)?.toInt()
        addFragment(R.id.container, ReservationSummaryFragment.newInstance(parkingSpotId))
    }

    override fun navigateToMainActivity() {
        finish()
    }

}