package com.example.licenta_mobile.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.licenta_mobile.R;

import java.util.ArrayList;
import java.util.List;

public class ReservationDialog extends Dialog {

    private Activity activity;
    private List<String> licensePlates = new ArrayList<>();
    private int parkingPlaceId;
    private EditText introducedLicensePlate;
    private Spinner selectedLicensePlate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.reservation_dialog);

        Spinner selectedNumber = findViewById(R.id.licensePlateSelector);
        selectedNumber.setSelection(0);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, licensePlates);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectedNumber.setAdapter(dataAdapter);
    }

    public ReservationDialog(Activity activity, int parkingPlaceId, List<String> licensePlates) {
        super(activity);
        this.licensePlates = licensePlates;
        this.parkingPlaceId = parkingPlaceId;
    }



}
