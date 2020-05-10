package com.example.licenta_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.licenta_mobile.adapter.PendingReservationAdapter;
import com.example.licenta_mobile.dto.UnconfirmedReservationDto;
import com.example.licenta_mobile.model.UserData;
import com.example.licenta_mobile.rest.ReservationService;
import com.example.licenta_mobile.rest.RestClient;
import com.example.licenta_mobile.security.Token;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActiveReservationsActivity extends AppCompatActivity {

    private ReservationService reservationService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations);
        reservationService = RestClient.getClient().create(ReservationService.class);
        TextView viewTitle = findViewById(R.id.reservationTitle);
        viewTitle.setText("Reservation History");
        handleReservations();
    }

    private void handleReservations(){
        int userId = UserData.getUserId();
        Call<List<UnconfirmedReservationDto>> call = reservationService.getAllReservedPlaces("Bearer " + Token.getJwtToken(), userId);
        call.enqueue(new Callback<List<UnconfirmedReservationDto>>() {

            @Override
            public void onResponse(Call<List<UnconfirmedReservationDto>> call, Response<List<UnconfirmedReservationDto>> response) {
                if (response.isSuccessful()) {
                    List<UnconfirmedReservationDto> unconfirmedReservations = response.body();
                    buildView(unconfirmedReservations);
                }
            }

            @Override
            public void onFailure(Call<List<UnconfirmedReservationDto>> call, Throwable t) {
                System.out.println(t.getMessage());
                call.cancel();
            }
        });
    }

    private void buildView(List<UnconfirmedReservationDto> pendingReservations){
        NotificationHandler notificationHandler = new NotificationHandler(this);
        PendingReservationAdapter adapter = new PendingReservationAdapter(pendingReservations, this, notificationHandler, this);
        ListView lView = findViewById(R.id.pendingReservationList);
        lView.setAdapter(adapter);
    }

}
