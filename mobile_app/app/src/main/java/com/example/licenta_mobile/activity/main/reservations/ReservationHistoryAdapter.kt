package com.example.licenta_mobile.activity.main.reservations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.licenta_mobile.R
import com.example.licenta_mobile.dto.ReservationDto


class ReservationHistoryAdapter(private val reservationsList: List<ReservationDto>,
                                private val reservationCancelListener: (resId: ReservationDto) -> Unit,
                                private val showQRCodeListener: (resId: String) -> Unit
) : RecyclerView.Adapter<ReservationHistoryAdapter.MyViewHolder>() {

    inner class MyViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val licensePlate: TextView = itemView.findViewById(R.id.license_plate)
        val cancelReservation: Button = itemView.findViewById(R.id.btn_cancel_reservation)
        val showQRCode: Button = itemView.findViewById(R.id.btn_view_QR)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationHistoryAdapter.MyViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val reservationView = inflater.inflate(R.layout.reservation_details, parent, false)
        return MyViewHolder(reservationView)
    }

    override fun onBindViewHolder(viewHolder: ReservationHistoryAdapter.MyViewHolder, position: Int) {
        viewHolder.licensePlate.text = reservationsList[position].licensePlate
        if (reservationsList[position].status == "finished" || reservationsList[position].status == "cancelled") {
            viewHolder.cancelReservation.visibility = View.INVISIBLE
        } else {
            viewHolder.cancelReservation.setOnClickListener {
                reservationCancelListener.invoke(reservationsList[position])
                notifyDataSetChanged()
            }
        }
        viewHolder.showQRCode.setOnClickListener { showQRCodeListener.invoke("${reservationsList[position].id}") }
    }

    override fun getItemCount(): Int {
        return reservationsList.size
    }
}