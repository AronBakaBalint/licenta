package com.example.licenta_mobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licenta_mobile.NotificationHandler;
import com.example.licenta_mobile.ParkingActivity;
import com.example.licenta_mobile.R;
import com.example.licenta_mobile.dto.MessageDto;
import com.example.licenta_mobile.dto.UnconfirmedReservationDto;
import com.example.licenta_mobile.rest.ReservationService;
import com.example.licenta_mobile.rest.RestClient;
import com.example.licenta_mobile.security.Token;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingReservationAdapter extends BaseAdapter implements ListAdapter {

    private List<UnconfirmedReservationDto> list = new ArrayList<>();
    private ReservationService reservationService;
    private NotificationHandler notificationHandler;
    private Context context;

    public PendingReservationAdapter(List<UnconfirmedReservationDto> list, Context context, NotificationHandler notificationHandler) {
        this.list = list;
        this.context = context;
        this.reservationService = RestClient.getClient().create(ReservationService.class);
        this.notificationHandler = notificationHandler;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return list.get(pos).getParkingPlaceId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.unconfirmed_reservations, null);
        }

        TextView licensePlate = view.findViewById(R.id.licensePlate);
        licensePlate.setText(list.get(position).getLicensePlate());

        //Handle buttons and add onClickListeners
        Button extendBtn = view.findViewById(R.id.extendReservation);
        Button cancelBtn = view.findViewById(R.id.cancelReservation);

        extendBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                System.out.println("Extended "+list.get(position).getParkingPlaceId()+" "+list.get(position).getLicensePlate());
                notificationHandler.startCountdownForNotification(list.get(position).getParkingPlaceId());
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                cancelReservation(list.get(position).getParkingPlaceId());
                list.remove(position);
                notifyDataSetChanged();
            }
        });

        return view;
    }

    private void cancelReservation(int parkintPlaceId){
        Call<MessageDto> call = reservationService.cancelReservation("Bearer "+Token.getJwtToken() , parkintPlaceId);
        call.enqueue(new Callback<MessageDto>(){

            @Override
            public void onResponse(Call<MessageDto> call, Response<MessageDto> response) {
                if (response.isSuccessful()) {
                    String message = response.body().getMessage();
                    if("ok".equals(message)){
                        Toast.makeText(context, "Reservation canceled", Toast.LENGTH_SHORT).show();
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
}
