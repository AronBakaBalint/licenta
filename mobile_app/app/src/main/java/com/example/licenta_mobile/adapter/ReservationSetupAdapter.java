package com.example.licenta_mobile.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;

import com.example.licenta_mobile.R;
import com.example.licenta_mobile.dto.ReservationDto;

import java.util.ArrayList;
import java.util.List;

public class ReservationSetupAdapter extends BaseAdapter implements ListAdapter {

    private List<String> hoursList = new ArrayList<>();
    private Activity activity;

    public ReservationSetupAdapter(List<String> hoursList, Activity activity) {
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
        return view;
    }

}
