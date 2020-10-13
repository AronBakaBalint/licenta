package com.example.licenta_mobile.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListAdapter
import com.example.licenta_mobile.R

class ReservationSetupAdapter(private val hoursList: List<String>, private val occupiedHours: List<Int>, private val selectedHours: MutableList<Button>, private val activity: Activity) : BaseAdapter(), ListAdapter {

    override fun getCount(): Int {
        return hoursList.size
    }

    override fun getItem(i: Int): Any {
        return hoursList[i]
    }

    override fun getItemId(i: Int): Long {
        return hoursList[i].substring(0, 2).toInt().toLong()
    }

    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.occupied_hours, null)
        val hourBtn = view.findViewById<Button>(R.id.hourBtn)

        hourBtn.setOnClickListener {v -> handleHourPressed(v as Button) }

        hourBtn.text = hoursList[position]
        if (isCorrespondingHourOccupied(hoursList[position])) {
            hourBtn.isClickable = false
            hourBtn.setBackgroundColor(Color.RED)
        } else {
            hourBtn.setBackgroundColor(Color.GREEN)
        }
        return view
    }

    private fun handleHourPressed(b : Button){
        if(selectedHours.contains(b)){
            handleDeselectedHour(b)
        } else {
            handleSelectedHour(b)
        }
    }

    private fun handleSelectedHour(b : Button){
        selectedHours.add(b)
        b.setBackgroundColor(Color.YELLOW)
    }

    private fun handleDeselectedHour(b : Button){
        selectedHours.remove(b)
        b.setBackgroundColor(Color.GREEN)
    }

    private fun isCorrespondingHourOccupied(btnHour: String): Boolean {
        for (i in occupiedHours) {
            if (btnHour.substring(0, 2).toInt() == i) {
                return true
            }
        }
        return false
    }
}