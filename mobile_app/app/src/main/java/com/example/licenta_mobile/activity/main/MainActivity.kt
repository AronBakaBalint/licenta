package com.example.licenta_mobile.activity.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.licenta_mobile.R
import com.example.licenta_mobile.activity.reservation.SpotReservationActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), MainCommandListener {

    companion object {
        const val PARKING_SPOT_ID_EXTRA = "spotId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
                setOf(
                        R.id.navigation_home,
                        R.id.navigation_reservations,
                        R.id.navigation_profile,
                )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onGoToReservation(spotId: Int) {
        val intent = Intent(this, SpotReservationActivity::class.java)
        intent.putExtra(PARKING_SPOT_ID_EXTRA, "$spotId")
        startActivity(intent)
    }

}