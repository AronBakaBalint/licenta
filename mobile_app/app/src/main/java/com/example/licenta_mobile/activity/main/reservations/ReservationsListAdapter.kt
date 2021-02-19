package com.example.licenta_mobile.activity.main.reservations

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListAdapter
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.licenta_mobile.R
import com.example.licenta_mobile.dto.ReservationDto


class ReservationsListAdapter(private val list: List<ReservationDto>, private val activity: Activity) : BaseAdapter(), ListAdapter {

    private var _qrCodeToDisplay = MutableLiveData<String>()
    var qrCodeToDisplay: LiveData<String> = _qrCodeToDisplay

    private var _reservationToCancel = MutableLiveData<ReservationDto>()
    var reservationToCancel: LiveData<ReservationDto> = _reservationToCancel

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(pos: Int): Any {
        return list[pos]
    }

    override fun getItemId(pos: Int): Long {
        return list[pos].parkingSpotId.toLong()
    }

    @SuppressLint("DefaultLocale", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            val inflater = activity.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.reservation_details, null)
        }

        val licensePlate = view!!.findViewById<TextView>(R.id.licensePlate)
        licensePlate.text = list[position].licensePlate.toUpperCase()

        //Handle buttons and add onClickListeners
        val cancelBtn = view.findViewById<Button>(R.id.cancelReservation)
        if (list[position].status == "cancelled" || list[position].status == "finished") {
            hide(cancelBtn)
        }
        val qrCodeBtn = view.findViewById<Button>(R.id.viewQR)
        cancelBtn.setOnClickListener { _reservationToCancel.value = list[position] }
        qrCodeBtn.setOnClickListener { _qrCodeToDisplay.value = list[position].id.toString() }
        return view
    }

    private fun hide(view: View?){
        view!!.visibility = View.INVISIBLE
    }

}