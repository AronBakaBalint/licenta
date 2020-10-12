package com.example.licenta_mobile.dialog

import android.content.Context
import androidx.appcompat.app.AlertDialog

object NotEnoughMoneyDialog {
    fun show(context: Context?) {
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle("Not Enough Money")
        builder.setMessage("It seems you don't have enough money.\nGo to options -> profile and transfer some money")
        builder.setCancelable(true)
        val alert = builder.create()
        alert.show()
    }
}