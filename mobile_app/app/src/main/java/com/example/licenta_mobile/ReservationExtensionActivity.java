package com.example.licenta_mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.licenta_mobile.dialog.ReservationExtensionDialog;
import com.example.licenta_mobile.dto.MessageDto;
import com.example.licenta_mobile.dto.ParkingPlaceDto;
import com.example.licenta_mobile.dto.ReservationDto;
import com.example.licenta_mobile.rest.ReservationService;
import com.example.licenta_mobile.rest.RestClient;
import com.example.licenta_mobile.security.Token;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationExtensionActivity extends AppCompatActivity {

    private ReservationService reservationService;
    private int parkingPlaceId;
    private Dialog reservationExtensionDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_extension);
        parkingPlaceId = Global.parkinPlaceId;
        reservationService = RestClient.getClient().create(ReservationService.class);
        showReservationExtensionDialog();
    }

    private void showReservationExtensionDialog(){
        reservationExtensionDialog = new ReservationExtensionDialog(this);
        reservationExtensionDialog.show();
    }

    public void extendReservation(View view){
        Toast.makeText(ReservationExtensionActivity.this, "Reservation extended", Toast.LENGTH_SHORT).show();
        reservationExtensionDialog.dismiss();
        Intent intent = new Intent(ReservationExtensionActivity.this, ParkingActivity.class);
        startActivity(intent);
        startCountDown(parkingPlaceId);
    }

    public void cancelReservation(View view){
        Call<MessageDto> call = reservationService.cancelReservation("Bearer " + Token.getJwtToken(), parkingPlaceId);
        call.enqueue(new Callback<MessageDto>() {

            @Override
            public void onResponse(Call<MessageDto> call, Response<MessageDto> response) {
                if (response.isSuccessful()) {
                    if(response.body().getMessage().contains("ok")){
                        Toast.makeText(ReservationExtensionActivity.this, "Reservation canceled", Toast.LENGTH_SHORT).show();
                        reservationExtensionDialog.dismiss();
                        Intent intent = new Intent(ReservationExtensionActivity.this, ParkingActivity.class);
                        startActivity(intent);
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
