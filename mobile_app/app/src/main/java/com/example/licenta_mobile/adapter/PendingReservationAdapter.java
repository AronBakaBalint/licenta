package com.example.licenta_mobile.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.licenta_mobile.NotificationHandler;
import com.example.licenta_mobile.R;
import com.example.licenta_mobile.dialog.NotEnoughMoneyDialog;
import com.example.licenta_mobile.dto.MessageDto;
import com.example.licenta_mobile.dto.ReservationDto;
import com.example.licenta_mobile.model.UserData;
import com.example.licenta_mobile.rest.ReservationService;
import com.example.licenta_mobile.rest.RestClient;
import com.example.licenta_mobile.security.Token;
import com.google.zxing.WriterException;

import java.util.ArrayList;
import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingReservationAdapter extends BaseAdapter implements ListAdapter {

    private List<ReservationDto> list = new ArrayList<>();
    private ReservationService reservationService;
    private NotificationHandler notificationHandler;
    private Activity activity;

    public PendingReservationAdapter(List<ReservationDto> list, NotificationHandler notificationHandler, Activity activity) {
        this.list = list;
        this.reservationService = RestClient.getClient().create(ReservationService.class);
        this.notificationHandler = notificationHandler;
        this.activity = activity;
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
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.unconfirmed_reservations, null);
        }

        TextView licensePlate = view.findViewById(R.id.licensePlate);
        licensePlate.setText(list.get(position).getLicensePlate().toUpperCase());

        //Handle buttons and add onClickListeners
        Button extendBtn = view.findViewById(R.id.extendReservation);
        Button cancelBtn = view.findViewById(R.id.cancelReservation);

        if(list.get(position).getStatus().equals("cancelled") || list.get(position).getStatus().equals("finished")){
            extendBtn.setVisibility(View.INVISIBLE);
            cancelBtn.setVisibility(View.INVISIBLE);
        }
        final Button qrCodeBtn = view.findViewById(R.id.viewQR);

        extendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extendReservation(list.get(position).getReservationId());
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelReservation(list.get(position));
            }
        });

        qrCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQRCodeDialog(list.get(position).getReservationId()+"", qrCodeBtn);
            }
        });

        return view;
    }

    private void cancelReservation(final ReservationDto reservationDto) {
        Call<Void> call = reservationService.cancelReservation("Bearer " + Token.getJwtToken(), reservationDto.getReservationId());
        call.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(activity, "Reservation cancelled", Toast.LENGTH_SHORT).show();
                    list.remove(reservationDto);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println(t.getMessage());
                call.cancel();
            }
        });
    }

    //source
    //https://www.c-sharpcorner.com/article/how-to-generate-qr-code-in-android/
    private void showQRCodeDialog(String reservationId, View view) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = (int) (size.x * 0.95);
        QRGEncoder qrgEncoder = new QRGEncoder(reservationId, null, QRGContents.Type.TEXT, width);

        Dialog builder = new Dialog(view.getContext());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(view.getContext());
        try {
            imageView.setImageBitmap(qrgEncoder.encodeAsBitmap());
        } catch (WriterException e) {
            e.printStackTrace();
        }
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }

    private void extendReservation(final int reservationId) {
        final Double extensionCost = getExtensionCost();
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        builder1.setMessage("Extend reservation for an extra " + extensionCost + " lei?");
        builder1.setTitle("Extend Reservation");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        if (UserData.getCurrentSold() < extensionCost) {
                            NotEnoughMoneyDialog.show(activity);
                        } else {
                            sendExtendReservationRequest(extensionCost, reservationId);
                        }
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private Double getExtensionCost(){
        Double extensionCost = -1.0;
        Call<MessageDto> call = reservationService.getExtensionCost("Bearer " + Token.getJwtToken());
        try {
            Response<MessageDto> response = call.execute();
            extensionCost = Double.parseDouble(response.body().getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return extensionCost;
    }

    private void sendExtendReservationRequest(final Double extensionCost,final int reservationId) {
        Call<Void> call = reservationService.extendReservation("Bearer " + Token.getJwtToken(), reservationId);
        call.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    notificationHandler.startCountdownForNotification(reservationId);
                    UserData.setCurrentSold(UserData.getCurrentSold() - extensionCost);
                    Toast.makeText(activity, "Reservation extended", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println(t.getMessage());
                call.cancel();
            }
        });
    }
}
