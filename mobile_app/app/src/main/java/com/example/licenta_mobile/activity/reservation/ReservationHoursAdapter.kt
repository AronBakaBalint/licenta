package com.example.licenta_mobile.activity.reservation

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.licenta_mobile.R

class ReservationHoursAdapter (private val occupiedHours: List<Int>?,
                               private val selectedHours: List<Int>?,
                               private val hourClickListener: (hour: Int) -> Unit
    ) : RecyclerView.Adapter<ReservationHoursAdapter.MyViewHolder>() {

    private val reservationHours: List<String> = generateHoursList()

    inner class MyViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val hour: Button = itemView.findViewById(R.id.hourBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationHoursAdapter.MyViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val hourView = inflater.inflate(R.layout.occupied_hours, parent, false)
        return MyViewHolder(hourView)
    }

    override fun onBindViewHolder(viewHolder: ReservationHoursAdapter.MyViewHolder, position: Int) {
        val hour = viewHolder.hour
        hour.text = reservationHours[position]
        if(isHourOccupied(position)) {
            hour.isClickable = false
            hour.setBackgroundColor(Color.RED)
        } else {
            hour.isClickable = true
            hour.setOnClickListener {
                hourClickListener.invoke(position)
                notifyDataSetChanged()
            }
            if(isHourSelected(position)) {
                hour.setBackgroundColor(Color.YELLOW)
            } else {
                hour.setBackgroundColor(Color.GREEN)
            }
        }
    }

    override fun getItemCount(): Int {
        return reservationHours.size
    }

    private fun isHourOccupied(position: Int): Boolean {
        return occupiedHours?.contains(position)!!
    }

    private fun isHourSelected(position: Int): Boolean {
        return selectedHours?.contains(position)!!
    }

    private fun generateHoursList(): List<String> {
        val hoursList = arrayListOf<String>()
        for(i in 0..23) {
            if(i < 10) {
                hoursList.add("0$i:00")
            } else {
                hoursList.add("$i:00")
            }
        }
        return hoursList
    }
}