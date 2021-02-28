package com.example.licenta_mobile.activity.reservation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.licenta_mobile.R

class SpotReservationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spot_reservation)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SpotReservationFragment.newInstance())
                    .commitNow()
        }
    }
}