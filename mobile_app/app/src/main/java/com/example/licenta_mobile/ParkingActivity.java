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
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licenta_mobile.adapter.PendingReservationAdapter;
import com.example.licenta_mobile.dialog.ReservationDialog;
import com.example.licenta_mobile.dto.JwtTokenDto;
import com.example.licenta_mobile.dto.MessageDto;
import com.example.licenta_mobile.dto.ParkingPlaceDto;
import com.example.licenta_mobile.dto.ReservationDto;
import com.example.licenta_mobile.dto.UnconfirmedReservationDto;
import com.example.licenta_mobile.model.UserData;
import com.example.licenta_mobile.rest.ReservationService;
import com.example.licenta_mobile.rest.RestClient;
import com.example.licenta_mobile.security.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class ParkingActivity extends AppCompatActivity {

    private ReservationService reservationService;
    private ReservationDialog reservationDialog;
    private NotificationHandler notificationHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserData.update();
        setContentView(R.layout.activity_parking_place_selector);
        reservationService = RestClient.getClient().create(ReservationService.class);
        notificationHandler = new NotificationHandler(this);
        displayParkingPlaceStatus();
    }

    @Override
    public void onRestart(){
        super.onRestart();
        UserData.update();
        setContentView(R.layout.activity_parking_place_selector);
        reservationService = RestClient.getClient().create(ReservationService.class);
        notificationHandler = new NotificationHandler(this);
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
        reservationDto.setUserId(UserData.getUserId());
        reservationDto.setLicensePlate(licensePlate);
        reservationDialog.dismiss();

        Call<MessageDto> call = reservationService.reserveParkingPlace("Bearer "+Token.getJwtToken() ,reservationDto);
        call.enqueue(new Callback<MessageDto>(){

            @Override
            public void onResponse(Call<MessageDto> call, Response<MessageDto> response) {
                if (response.isSuccessful()) {
                    int reservationId = Integer.parseInt(response.body().getMessage());
                    System.out.println(reservationId);
                    showReservationInfoDialog(reservationId);
                }
            }

            @Override
            public void onFailure(Call<MessageDto> call, Throwable t) {
                System.out.println(t.getMessage());
                call.cancel();
            }
        });
    }

    private void showReservationInfoDialog(final int reservationId){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your reservation has been made. You can find the code which has to be shown at the entrance in the reservation history.")
                .setCancelable(false)
                .setTitle("Reservation Info")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        startActivity(getIntent());
                        notificationHandler.startCountdownForNotification(reservationId);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.username_item);
        menuItem.setTitle(UserData.getUserName());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        //sign out operation
        if (id == R.id.profile) {
            viewProfile();
            return true;

        } else if (id == R.id.reservations) {
            viewReservations();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void viewProfile(){

    }

    private void viewReservations(){
        int userId = UserData.getUserId();
        Call<List<UnconfirmedReservationDto>> call = reservationService.getAllReservedPlaces("Bearer " + Token.getJwtToken(), userId);
        call.enqueue(new Callback<List<UnconfirmedReservationDto>>() {

            @Override
            public void onResponse(Call<List<UnconfirmedReservationDto>> call, Response<List<UnconfirmedReservationDto>> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(ParkingActivity.this, ActiveReservationsActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<List<UnconfirmedReservationDto>> call, Throwable t) {
                System.out.println(t.getMessage());
                call.cancel();
            }
        });
    }
}
