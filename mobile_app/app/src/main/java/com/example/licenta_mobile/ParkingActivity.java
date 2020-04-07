package com.example.licenta_mobile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.licenta_mobile.dto.ParkingPlaceDto;
import com.example.licenta_mobile.rest.ReservationService;
import com.example.licenta_mobile.rest.RestClient;
import com.example.licenta_mobile.security.Token;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParkingActivity extends AppCompatActivity {

    private ReservationService reservationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_place_selector);
        reservationService = RestClient.getClient().create(ReservationService.class);
        displayParkingPlaceStatus();
    }

    private void displayParkingPlaceStatus(){

        System.out.println(Token.getJwtToken());
        Call<List<ParkingPlaceDto>> call = reservationService.getAllParkingPlaces("Bearer "+Token.getJwtToken());
        call.enqueue(new Callback<List<ParkingPlaceDto>>(){

            @Override
            public void onResponse(Call<List<ParkingPlaceDto>> call, Response<List<ParkingPlaceDto>> response) {
                if (response.isSuccessful()) {
                    List<ParkingPlaceDto> parkingPlaces = response.body();
                    setParkingPlaceColors(parkingPlaces);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                System.out.println(t.getMessage());
                call.cancel();
            }
        });
    }

    private void setParkingPlaceColors(List<ParkingPlaceDto> parkingPlaces){
        ViewGroup viewGroup = (ViewGroup) getWindow().getDecorView();
        List<Button> uiParkingPlaces = getAllParkingPlacesFromUI();
        System.out.println(uiParkingPlaces.size());
        for(int i=0;i < uiParkingPlaces.size(); i++){
            uiParkingPlaces.get(i).setBackgroundColor(statusToColor(parkingPlaces.get(i).getStatus()));
            if(!parkingPlaces.get(i).getStatus().equals("free")){
                uiParkingPlaces.get(i).setClickable(false);
            }
        }
    }

    private List<Button> getAllParkingPlacesFromUI(){
        List<Button> parkingPlaces = new ArrayList<>();

        LinearLayout layout = findViewById(R.id.row1);
        for(int i =0; i< layout.getChildCount(); i++){
            View v =layout.getChildAt(i);
            if(v instanceof Button){
                v.setId(i+1);
                parkingPlaces.add((Button) v);
            }
        }

        layout = findViewById(R.id.row2);
        for(int i =0; i< layout.getChildCount(); i++){
            View v =layout.getChildAt(i);
            if(v instanceof Button){
                v.setId(layout.getChildCount()+i+1);
                parkingPlaces.add((Button) v);
            }
        }

        layout = findViewById(R.id.row3);
        for(int i =0; i< layout.getChildCount(); i++){
            View v =layout.getChildAt(i);
            if(v instanceof Button){
                v.setId(2*layout.getChildCount()+i+1);
                parkingPlaces.add((Button) v);
            }
        }

        layout = findViewById(R.id.row4);
        for(int i =0; i< layout.getChildCount(); i++){
            View v =layout.getChildAt(i);
            if(v instanceof Button){
                v.setId(3*layout.getChildCount()+i+1);
                parkingPlaces.add((Button) v);
            }
        }
        return parkingPlaces;
    }

    private int statusToColor(String status){
        switch(status){
            case "free": return getIntFromColor(0,255,0);
            case "reserved": return getIntFromColor(255,255,0);
            case "occupied": return getIntFromColor(255,0,0);
        }
        return 0;
    }

    private int getIntFromColor(int Red, int Green, int Blue){
        Red = (Red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        Green = (Green << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        Blue = Blue & 0x000000FF; //Mask out anything not blue.

        return 0xFF000000 | Red | Green | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }

    public void startReservation(View view) {
        final int parkingPlaceId = view.getId();
        String parkingPlaceName = ((Button) view).getText().toString();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Reservation");
        builder.setMessage("Reserve parking place "+parkingPlaceName+"?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                startReservationActivity(parkingPlaceId);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void startReservationActivity(int parkingPlaceId){
        System.out.println(parkingPlaceId);
    }
}
