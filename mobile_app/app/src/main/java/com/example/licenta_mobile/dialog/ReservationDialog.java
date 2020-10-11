package com.example.licenta_mobile.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
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
import com.example.licenta_mobile.rest.ReservationService;
import com.example.licenta_mobile.rest.RestClient;
import com.example.licenta_mobile.security.Token;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ReservationDialog extends Dialog {

    private int parkingPlaceId;
    private EditText introducedLicensePlate;
    private SimpleDate selectedDate;
    private Activity activity;
    private ReservationService service = null;
    private ListView listView;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.reservation_dialog);
        listView = findViewById(R.id.hourList);
        getWindow()
                .setLayout(
                        ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
        introducedLicensePlate = findViewById(R.id.licensePlateEditor);
        service = RestClient.getClient().create(ReservationService.class);
        DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                selectedDate = new SimpleDate(dayOfMonth, month+1, year);
                List<Integer> occupiedHours = getReservationSchedule(parkingPlaceId, selectedDate);
                listView.setAdapter(new ReservationSetupAdapter(get24HoursList(), occupiedHours, activity));
            }
        });
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        selectedDate = new SimpleDate(day, month + 1, year);
        List<Integer> occupiedHours = getReservationSchedule(parkingPlaceId, selectedDate);
        listView.setAdapter(new ReservationSetupAdapter(get24HoursList(), occupiedHours, activity));
    }

    private List<Integer> getReservationSchedule(int parkingPlaceId, SimpleDate reservationDate) {
        List<Integer> reservationList = new ArrayList<>();
        Call<List<ReservationDto>> callSync = service.getAllActiveReservations("Bearer " + Token.getJwtToken(), parkingPlaceId, selectedDate);

        try {
            Response<List<ReservationDto>> response = callSync.execute();
            List<ReservationDto> apiResponse = response.body();
            for (ReservationDto reservation : apiResponse) {
                for (Integer d : reservation.getDuration()) {
                    reservationList.add(d);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return reservationList;
    }

    private List<String> get24HoursList() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i <= 23; i++) {
            list.add(i < 10 ? "0" + i + ":00" : i + ":00");
        }
        return list;
    }

}
