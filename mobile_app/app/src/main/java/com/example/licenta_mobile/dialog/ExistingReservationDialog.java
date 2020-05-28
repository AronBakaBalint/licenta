package com.example.licenta_mobile.dialog;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class ExistingReservationDialog {

    public static void show(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("There is already a pending reservation for this license plate.\nFinish the existing reservation first in order to be able to make a new one")
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
