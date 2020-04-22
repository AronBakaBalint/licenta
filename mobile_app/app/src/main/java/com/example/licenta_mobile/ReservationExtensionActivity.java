package com.example.licenta_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ReservationExtensionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_extension);

        System.out.println(getIntent().getStringExtra("parkingPlaceId"));
    }
}
