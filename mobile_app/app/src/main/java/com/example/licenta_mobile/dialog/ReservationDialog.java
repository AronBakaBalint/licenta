package com.example.licenta_mobile.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;

import com.example.licenta_mobile.R;
import com.example.licenta_mobile.adapter.ReservationSetupAdapter;
import com.example.licenta_mobile.dto.ReservationDto;
import com.example.licenta_mobile.model.SimpleDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReservationDialog extends Dialog {

    private int parkingPlaceId;
    private EditText introducedLicensePlate;
    private SimpleDate selectedDate;
    private Activity activity;

    public ReservationDialog(Activity activity, int parkingPlaceId) {
        super(activity);
        this.activity = activity;
        this.parkingPlaceId = parkingPlaceId;
    }

    public int getParkingPlaceId() {
        return parkingPlaceId;
    }

    public void setParkingPlaceId(int parkingPlaceId) {
        this.parkingPlaceId = parkingPlaceId;
    }

    public EditText getIntroducedLicensePlate() {
        return introducedLicensePlate;
    }

    public void setIntroducedLicensePlate(EditText introducedLicensePlate) {
        this.introducedLicensePlate = introducedLicensePlate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.reservation_dialog);
        introducedLicensePlate = findViewById(R.id.licensePlateEditor);
        DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                selectedDate = new SimpleDate(dayOfMonth, month, year);
            }
        });
        ListView listView = findViewById(R.id.hourList);
        listView.setAdapter(new ReservationSetupAdapter(get24HoursList(), activity));
    }

    private List<String> get24HoursList(){
        List<String> list = new ArrayList<>();
        for(int i = 0;i <= 23; i++) {
            list.add( i < 10 ? "0" + i + ":00" : i + ":00");
        }
        return list;
    }

}
