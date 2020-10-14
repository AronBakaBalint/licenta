package com.example.licenta_mobile.dialog

import android.content.Context
import androidx.appcompat.app.AlertDialog

object ExistingReservationDialog {

    fun show(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage("There is already a pending reservation for this license plate.\nFinish the existing reservation first in order to be able to make a new one.")
                .setCancelable(false)
                .setTitle("Reservation Info")
                .setPositiveButton("OK") { _, _ -> }
        val alert = builder.create()
        alert.show()
    }
}