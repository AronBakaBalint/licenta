package com.example.licenta_mobile

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Handler
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.licenta_mobile.dto.MessageDto
import com.example.licenta_mobile.rest.ReservationService
import com.example.licenta_mobile.rest.RestClient
import com.example.licenta_mobile.security.Token
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationHandler(private val activity: Activity) {

    private val reservationService: ReservationService = RestClient.client!!.create(ReservationService::class.java)

    fun startCountdownForNotification(parkingPlaceId: Int) {
        startCountDown(parkingPlaceId)
    }

    private fun startCountDown(reservationId: Int) {
        val handler = Handler()
        handler.postDelayed({ checkArrived(reservationId) }, 10000)
    }

    private fun checkArrived(reservationId: Int) {
        val call = reservationService.getReservationStatus("Bearer " + Token.jwtToken, reservationId)
        call.enqueue(object : Callback<MessageDto> {
            override fun onResponse(call: Call<MessageDto>, response: Response<MessageDto>) {
                if (response.isSuccessful) {
                    if (response.body()!!.message == "reserved") {
                        showNotification()
                    }
                }
            }

            override fun onFailure(call: Call<MessageDto>, t: Throwable) {
                println(t.message)
                call.cancel()
            }
        })
    }

    //source
    //https://developer.android.com/training/notify-user/build-notification
    private fun showNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel("myNotification", "myNotification", NotificationManager.IMPORTANCE_DEFAULT)
            val manager = activity.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(notificationChannel)
        }
        val intent = Intent(activity, ReservationExtensionActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(activity.applicationContext, "myNotification")
                .setSmallIcon(R.drawable.ic_local_parking_black_24dp)
                .setContentTitle("Reservation overdue")
                .setContentText("Tap for more details")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        val manager = NotificationManagerCompat.from(activity)
        manager.notify(999, builder.build())
    }

}