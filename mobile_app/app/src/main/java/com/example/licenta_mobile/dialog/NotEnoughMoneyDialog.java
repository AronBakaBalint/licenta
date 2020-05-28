package com.example.licenta_mobile.dialog;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

public class NotEnoughMoneyDialog {

    public static void show(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Not Enough Money");
        builder.setMessage("It seems you don't have enough money.\nGo to options -> profile and transfer some money");
        builder.setCancelable(true);
        AlertDialog alert = builder.create();
        alert.show();
    }
}
