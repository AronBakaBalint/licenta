package com.example.licenta_mobile.dialog

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.widget.EditText
import com.example.licenta_mobile.R

class MoneyTransferDialog(activity: Activity?) : Dialog(activity!!) {

    var introducedAmount: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.add_money_dialog)
        introducedAmount = findViewById(R.id.introducedAmount)
    }
}