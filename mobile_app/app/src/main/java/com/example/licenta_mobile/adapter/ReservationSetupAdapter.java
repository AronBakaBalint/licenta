package com.example.licenta_mobile.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;

import com.example.licenta_mobile.R;

import java.util.ArrayList;
import java.util.List;

public class ReservationSetupAdapter extends BaseAdapter implements ListAdapter {

    private List<String> hoursList;
    private List<Integer> occupiedHours;
    private Activity activity;

    public ReservationSetupAdapter(List<String> hoursList, List<Integer> occupiedHours, Activity activity) {
        this.occupiedHours = occupiedHours;
        this.hoursList = hoursList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return hoursList.size();
    }

    @Override
    public Object getItem(int i) {
        return hoursList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return Integer.parseInt(hoursList.get(i).substring(0, 2));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.occupied_hours, null);
        }

        Button hourBtn = view.findViewById(R.id.hourBtn);
        hourBtn.setText(hoursList.get(position));
        if(isCorrespondingHourOccupied(hoursList.get(position))){
            hourBtn.setClickable(false);
            hourBtn.setBackgroundColor(Color.RED);
        } else {
            hourBtn.setBackgroundColor(Color.GREEN);
        }
        return view;
    }

    private boolean isCorrespondingHourOccupied(String btnHour){
        for(Integer i : occupiedHours) {
            if(Integer.parseInt(btnHour.substring(0, 2)) == i){
                return true;
            }
        }
        return false;
    }

}
