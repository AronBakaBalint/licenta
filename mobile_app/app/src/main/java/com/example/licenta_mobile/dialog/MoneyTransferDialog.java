package com.example.licenta_mobile.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.licenta_mobile.R;

import java.util.List;

public class MoneyTransferDialog extends Dialog {

    private EditText introducedAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_money_dialog);
        introducedAmount = findViewById(R.id.introducedAmount);
    }

    public MoneyTransferDialog(Activity activity) {
        super(activity);
    }

    public EditText getIntroducedAmount() {
        return introducedAmount;
    }

    public void setIntroducedAmount(EditText introducedAmount) {
        this.introducedAmount = introducedAmount;
    }
}
