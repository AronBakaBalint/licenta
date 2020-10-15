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
import com.example.licenta_mobile.rest.ReservationService
import com.example.licenta_mobile.rest.RestClient.client
import com.example.licenta_mobile.security.Token
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

object NotificationScheduler {

    private lateinit var activity: Activity
    private var reservationId = -1
    private lateinit var reservationStartTime: Date
    private val reservationService = client!!.create(ReservationService::class.java)

    @JvmStatic
    fun start() {
        println("started $reservationStartTime")
    }

    private fun checkArrived(reservationId: Int) {
        val call = reservationService.isReservationPending("Bearer " + Token.jwtToken, reservationId)
        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    if (response.body()!!) {
                        showNotification()
                    }
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                println(t.message)
                call.cancel()
            }
        })
    }

    @JvmStatic
    fun builder(activity: Activity, reservationId: Int, reservationStartTime: Date): NotificationScheduler {
        this.activity = activity
        this.reservationId = reservationId
        this.reservationStartTime = reservationStartTime
        return this
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