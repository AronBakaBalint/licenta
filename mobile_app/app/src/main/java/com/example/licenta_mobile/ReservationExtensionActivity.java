package com.example.licenta_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.licenta_mobile.rest.ReservationService;
import com.example.licenta_mobile.rest.RestClient;

public class ReservationExtensionActivity extends AppCompatActivity {

    private ReservationService reservationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_extension);
        reservationService = RestClient.getClient().create(ReservationService.class);
    }


}
