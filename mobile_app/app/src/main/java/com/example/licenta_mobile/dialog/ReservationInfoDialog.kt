package com.example.licenta_mobile.dialog

import android.app.Activity
import androidx.appcompat.app.AlertDialog

object ReservationInfoDialog {
    fun show(context: Activity?) {
        val builder = AlertDialog.Builder(context!!)
        builder.setMessage("Your reservation has been made. You can find the code which has to be shown at the entrance in the reservation history.")
                .setCancelable(false)
                .setTitle("Reservation Info")
                .setPositiveButton("OK") { _, _ -> }
        val alert = builder.create()
        alert.show()
    }
}