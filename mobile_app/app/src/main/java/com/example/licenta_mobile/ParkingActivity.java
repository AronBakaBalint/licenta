package com.example.licenta_mobile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.licenta_mobile.dialog.ReservationDialog;
import com.example.licenta_mobile.dto.JwtTokenDto;
import com.example.licenta_mobile.dto.MessageDto;
import com.example.licenta_mobile.dto.ParkingPlaceDto;
import com.example.licenta_mobile.dto.ReservationDto;
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

    private ReservationDialog reservationDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_place_selector);
        reservationService = RestClient.getClient().create(ReservationService.class);
        displayParkingPlaceStatus();
    }

    private void displayParkingPlaceStatus(){

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
                showReservationDialog(parkingPlaceId);
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

    private void showReservationDialog(int parkingPlaceId){
        List<String> licensePlates = new ArrayList<>();
        licensePlates.add("No Item Selected");
        licensePlates.add("CJ25BBA");
        reservationDialog = new ReservationDialog(this, parkingPlaceId, licensePlates);
        reservationDialog.show();
    }

    public void confirmReservation(View view) {
        String licensePlate = reservationDialog.getIntroducedLicensePlate().getText().toString();
        final Integer parkingPlaceId = reservationDialog.getParkingPlaceId();
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setParkingPlaceId(parkingPlaceId);
        reservationDto.setLicensePlate(licensePlate);
        reservationDialog.dismiss();

        Call<MessageDto> call = reservationService.reserveParkingPlace("Bearer "+Token.getJwtToken() ,reservationDto);
        call.enqueue(new Callback<MessageDto>(){

            @Override
            public void onResponse(Call<MessageDto> call, Response<MessageDto> response) {
                if (response.isSuccessful()) {
                    String message = response.body().getMessage();
                    if("ok".equals(message)){
                        setParkingPlaceReserved(parkingPlaceId);
                        startCountDown(parkingPlaceId);
                    }
                }
            }

            @Override
            public void onFailure(Call<MessageDto> call, Throwable t) {
                System.out.println(t.getMessage());
                call.cancel();
            }
        });
    }

    private void setParkingPlaceReserved(int parkingPlaceId){
        List<Button> parkingPlaces = getAllParkingPlacesFromUI();
        for(int i=0;i < parkingPlaces.size(); i++){
            if(parkingPlaces.get(i).getId() == parkingPlaceId){
                parkingPlaces.get(i).setBackgroundColor(statusToColor("reserved"));
                parkingPlaces.get(i).setClickable(false);
            }
        }
    }

    private void startCountDown(int parkingId){
        final Handler handler = new Handler();
        final int parkingPlaceId = parkingId;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkArrived(parkingPlaceId);
            }
        }, 10000);
    }

    private void checkArrived(int parkingPlaceId) {
        Call<MessageDto> call = reservationService.getReservationStatus("Bearer " + Token.getJwtToken(), parkingPlaceId);
        call.enqueue(new Callback<MessageDto>() {

            @Override
            public void onResponse(Call<MessageDto> call, Response<MessageDto> response) {
                if (response.isSuccessful()) {
                    if(response.body().getMessage().contains("reserved")){
                        showNotification(Integer.parseInt(response.body().getMessage().replace("reserved", "")));
                    }
                }
            }

            @Override
            public void onFailure(Call<MessageDto> call, Throwable t) {
                System.out.println(t.getMessage());
                call.cancel();
            }
        });
    }

    private void showNotification(int parkingPlaceId){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ){
            NotificationChannel notificationChannel = new NotificationChannel("myNotification", "myNotification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }

        Intent intent = new Intent(this, ReservationExtensionActivity.class);
        intent.putExtra("parkingPlaceId", parkingPlaceId+"");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder  = new NotificationCompat.Builder(getApplicationContext(), "myNotification")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Reservation overdue")
            .setContentText("Tap here to extend or cancel reservation")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true);

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(999, builder.build());
    }
}
