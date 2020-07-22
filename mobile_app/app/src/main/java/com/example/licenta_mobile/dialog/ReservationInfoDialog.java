package com.example.licenta_mobile.dialog;

import android.app.Activity;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class ReservationInfoDialog {

    public static void show(final Activity context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Your reservation has been made. You can find the code which has to be shown at the entrance in the reservation history.")
                .setCancelable(false)
                .setTitle("Reservation Info")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
