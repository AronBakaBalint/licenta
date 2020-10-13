package com.example.licenta_mobile.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.appcompat.app.AlertDialog
import com.example.licenta_mobile.NotificationHandler
import com.example.licenta_mobile.R
import com.example.licenta_mobile.dialog.NotEnoughMoneyDialog
import com.example.licenta_mobile.dto.ReservationDto
import com.example.licenta_mobile.model.UserData.currentSold
import com.example.licenta_mobile.rest.ReservationService
import com.example.licenta_mobile.rest.RestClient
import com.example.licenta_mobile.security.Token
import com.google.zxing.WriterException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PendingReservationAdapter(private val list: MutableList<ReservationDto>?, private val notificationHandler: NotificationHandler, private val activity: Activity) : BaseAdapter(), ListAdapter {

    private val reservationService: ReservationService = RestClient.client!!.create(ReservationService::class.java)

    override fun getCount(): Int {
        return list!!.size
    }

    override fun getItem(pos: Int): Any {
        return list!![pos]
    }

    override fun getItemId(pos: Int): Long {
        return list!![pos].parkingSpotId.toLong()
    }

    @SuppressLint("DefaultLocale")
    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        val licensePlate = convertView.findViewById<TextView>(R.id.licensePlate)
        licensePlate.text = list!![position].licensePlate.toUpperCase()

        //Handle buttons and add onClickListeners
        val extendBtn = convertView.findViewById<Button>(R.id.extendReservation)
        val cancelBtn = convertView.findViewById<Button>(R.id.cancelReservation)
        if (list[position].status == "cancelled" || list[position].status == "finished") {
            extendBtn.visibility = View.INVISIBLE
            cancelBtn.visibility = View.INVISIBLE
        }
        val qrCodeBtn = convertView.findViewById<Button>(R.id.viewQR)
        extendBtn.setOnClickListener { extendReservation(list[position].id) }
        cancelBtn.setOnClickListener { cancelReservation(list[position]) }
        qrCodeBtn.setOnClickListener { showQRCodeDialog(list[position].id.toString() + "", qrCodeBtn) }
        return convertView
    }

    private fun cancelReservation(reservationDto: ReservationDto) {
        val call = reservationService.cancelReservation("Bearer " + Token.jwtToken, reservationDto.id)
        call!!.enqueue(object : Callback<Void?> {
            override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                if (response.isSuccessful) {
                    Toast.makeText(activity, "Reservation cancelled", Toast.LENGTH_SHORT).show()
                    list!!.remove(reservationDto)
                    notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<Void?>, t: Throwable) {
                println(t.message)
                call.cancel()
            }
        })
    }

    //source
    //https://www.c-sharpcorner.com/article/how-to-generate-qr-code-in-android/
    private fun showQRCodeDialog(reservationId: String, view: View) {
        val display = activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = (size.x * 0.95).toInt()
        val qrgEncoder = QRGEncoder(reservationId, null, QRGContents.Type.TEXT, width)
        val builder = Dialog(view.context)
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE)
        builder.window!!.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT))
        builder.setOnDismissListener {
            //nothing;
        }
        val imageView = ImageView(view.context)
        try {
            imageView.setImageBitmap(qrgEncoder.encodeAsBitmap())
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        builder.addContentView(imageView, RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT))
        builder.show()
    }

    private fun extendReservation(reservationId: Int) {
        val extensionCost = getExtensionCost()
        val builder1 = AlertDialog.Builder(activity)
        builder1.setMessage("Extend reservation for an extra $extensionCost lei?")
        builder1.setTitle("Extend Reservation")
        builder1.setCancelable(true)
        builder1.setPositiveButton(
                "Yes"
        ) { dialog, _ ->
            dialog.cancel()
            if (currentSold!! < extensionCost) {
                NotEnoughMoneyDialog.show(activity)
            } else {
                sendExtendReservationRequest(extensionCost, reservationId)
            }
        }
        builder1.setNegativeButton(
                "No"
        ) { dialog, _ -> dialog.cancel() }
        val alert11 = builder1.create()
        alert11.show()
    }

    private fun getExtensionCost(): Double {
        var extensionCost = -1.0
        val call = reservationService.getExtensionCost("Bearer " + Token.jwtToken)
        try {
            val response = call!!.execute()
            extensionCost = response.body()!!.message.toDouble()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return extensionCost
    }

    private fun sendExtendReservationRequest(extensionCost: Double, reservationId: Int) {
        val call = reservationService.extendReservation("Bearer " + Token.jwtToken, reservationId)
        call!!.enqueue(object : Callback<Void?> {
            override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                if (response.isSuccessful) {
                    notificationHandler.startCountdownForNotification(reservationId)
                    currentSold = currentSold!! - extensionCost
                    Toast.makeText(activity, "Reservation extended", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void?>, t: Throwable) {
                println(t.message)
                call.cancel()
            }
        })
    }

}