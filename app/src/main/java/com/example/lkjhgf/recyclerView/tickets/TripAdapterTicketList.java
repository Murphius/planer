package com.example.lkjhgf.recyclerView.tickets;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.example.lkjhgf.recyclerView.futureTrips.TripAdapter;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;
import com.example.lkjhgf.recyclerView.futureTrips.util.FutureTripViewHolder;

import java.util.ArrayList;

public class TripView extends TripAdapter {
    public TripView(ArrayList<TripItem> tripItems, Activity activity) {
        super(tripItems, activity);
    }

    @Override
    public void onBindViewHolder(@NonNull FutureTripViewHolder holder, int position) {
        super.onBindViewHolder(holder,position);
        holder.buttonClass.hideButtons();
    }
}
