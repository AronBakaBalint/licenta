package com.example.licenta_mobile;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.licenta_mobile.dto.MessageDto;
import com.example.licenta_mobile.rest.ReservationService;
import com.example.licenta_mobile.rest.RestClient;
import com.example.licenta_mobile.security.Token;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class NotificationHandler {


    private ReservationService reservationService;
    private Activity activity;

    public NotificationHandler(Activity activity) {
        this.reservationService = RestClient.getClient().create(ReservationService.class);
        this.activity = activity;
    }

    public void startCountdownForNotification(int parkingPlaceId) {
        startCountDown(parkingPlaceId);
    }

    private void startCountDown(final int reservationId) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkArrived(reservationId);
            }
        }, 10000);
    }

    private void checkArrived(final int reservationId) {
        Call<MessageDto> call = reservationService.getReservationStatus("Bearer " + Token.getJwtToken(), reservationId);
        call.enqueue(new Callback<MessageDto>() {
            @Override
            public void onResponse(Call<MessageDto> call, Response<MessageDto> response) {
                if (response.isSuccessful()) {
                    if (response.body().getMessage().equals("reserved")) {
                        showNotification();
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

    //source
    //https://developer.android.com/training/notify-user/build-notification
    private void showNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("myNotification", "myNotification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = activity.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }

        Intent intent = new Intent(activity, ReservationExtensionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity.getApplicationContext(), "myNotification")
                .setSmallIcon(R.drawable.ic_local_parking_black_24dp)
                .setContentTitle("Reservation overdue")
                .setContentText("Tap for more details")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat manager = NotificationManagerCompat.from(activity);
        manager.notify(999, builder.build());
    }
}
