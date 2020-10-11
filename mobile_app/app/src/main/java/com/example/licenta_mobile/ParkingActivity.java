package com.example.licenta_mobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.licenta_mobile.dialog.ExistingReservationDialog;
import com.example.licenta_mobile.dialog.NotEnoughMoneyDialog;
import com.example.licenta_mobile.dialog.ReservationDialog;
import com.example.licenta_mobile.dialog.ReservationInfoDialog;
import com.example.licenta_mobile.dto.MessageDto;
import com.example.licenta_mobile.dto.ParkingPlaceDto;
import com.example.licenta_mobile.dto.ReservationDto;
import com.example.licenta_mobile.model.UserData;
import com.example.licenta_mobile.rest.ReservationService;
import com.example.licenta_mobile.rest.RestClient;
import com.example.licenta_mobile.security.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParkingActivity extends AppCompatActivity {

    private ReservationService reservationService;
    private ReservationDialog reservationDialog;
    private NotificationHandler notificationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onInit();
    }

    @Override
    public void onRestart(){
        super.onRestart();
        onInit();
    }

    private void onInit(){
        UserData.update();
        setContentView(R.layout.activity_parking_place_selector);
        reservationService = RestClient.getClient().create(ReservationService.class);
        notificationHandler = new NotificationHandler(this);
        startAutoRefresh();
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

    private void startAutoRefresh(){
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                displayParkingPlaceStatus();
            }
        }, 0, 1000);//put here time 1000 milliseconds=1 second
    }

    private void setParkingPlaceColors(List<ParkingPlaceDto> parkingPlaces){
        List<Button> uiParkingPlaces = getAllParkingPlacesFromUI();
        for(int i=0;i < uiParkingPlaces.size(); i++){
            uiParkingPlaces.get(i).setBackgroundColor(parkingPlaces.get(i).getColor());
        }
    }

    private List<Button> getAllParkingPlacesFromUI(){
        List<Button> parkingPlaces = new ArrayList<>();

        LinearLayout layout = findViewById(R.id.row1);
        buildRow(layout, 0, parkingPlaces);
        layout = findViewById(R.id.row2);
        buildRow(layout, 1, parkingPlaces);
        layout = findViewById(R.id.row3);
        buildRow(layout, 2, parkingPlaces);
        layout = findViewById(R.id.row4);
        buildRow(layout, 3, parkingPlaces);

        return parkingPlaces;
    }

    public void startReservation(View view) {
        final int parkingPlaceId = view.getId();
        final Double reservationCost = getReservationCost();
        String parkingPlaceName = ((Button) view).getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reservation");
        builder.setMessage("Reserve parking place "+parkingPlaceName+"?\nInital cost is "+ reservationCost +" LEI but you will get it back as you arrive to the parking lot.");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if(UserData.getCurrentSold() > reservationCost){
                    showReservationDialog(parkingPlaceId);
                } else {
                    NotEnoughMoneyDialog.show(ParkingActivity.this);
                }
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
        reservationDialog = new ReservationDialog(this, parkingPlaceId);
        reservationDialog.show();
    }

    private Double getReservationCost(){
        Double reservationCost = -1.0;
        Call<MessageDto> call = reservationService.getReservationCost("Bearer " + Token.getJwtToken());
        try {
            Response<MessageDto> response = call.execute();
            reservationCost = Double.parseDouble(response.body().getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return reservationCost;
    }

    public void confirmReservation(View view) {
        String introducedLicensePlate = reservationDialog.getIntroducedLicensePlate().getText().toString();
        Integer parkingPlaceId = reservationDialog.getParkingPlaceId();
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setParkingPlaceId(parkingPlaceId);
        reservationDto.setUserId(UserData.getUserId());
        reservationDto.setLicensePlate(introducedLicensePlate);

        reservationDialog.dismiss();

        Call<MessageDto> call = reservationService.reserveParkingPlace("Bearer "+Token.getJwtToken() ,reservationDto);
        call.enqueue(new Callback<MessageDto>(){

            @Override
            public void onResponse(Call<MessageDto> call, Response<MessageDto> response) {
                if (response.isSuccessful()) {
                    int reservationId = Integer.parseInt(response.body().getMessage());
                    if (reservationId == -1){
                        ExistingReservationDialog.show(ParkingActivity.this);
                    } else {
                        ReservationInfoDialog.show(ParkingActivity.this);
                        notificationHandler.startCountdownForNotification(reservationId);
                        UserData.update();
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
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    private void viewReservations(){
        int userId = UserData.getUserId();
        Call<List<ReservationDto>> call = reservationService.getAllReservedPlaces("Bearer " + Token.getJwtToken(), userId);
        call.enqueue(new Callback<List<ReservationDto>>() {

            @Override
            public void onResponse(Call<List<ReservationDto>> call, Response<List<ReservationDto>> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(ParkingActivity.this, ActiveReservationsActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<List<ReservationDto>> call, Throwable t) {
                System.out.println(t.getMessage());
                call.cancel();
            }
        });
    }

    private void buildRow(LinearLayout layout, Integer row, List<Button> parkingPlaces){
        for(int i =0; i< layout.getChildCount(); i++){
            View v =layout.getChildAt(i);
            if(v instanceof Button){
                v.setId(row*layout.getChildCount()+i+1);
                parkingPlaces.add((Button) v);
            }
        }
    }
}
