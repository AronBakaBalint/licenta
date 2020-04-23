package com.example.licenta_mobile.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.example.licenta_mobile.R;

public class ReservationExtensionDialog extends Dialog {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.reservation_extension_dialog);
    }

    public ReservationExtensionDialog(@NonNull Context context) {
        super(context);
    }
}
