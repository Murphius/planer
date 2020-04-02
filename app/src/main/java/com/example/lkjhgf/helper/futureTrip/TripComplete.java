package com.example.lkjhgf.helper.futureTrip;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.futureTrips.recyclerView.FutureTrip;
import com.example.lkjhgf.futureTrips.recyclerView.TripItem;
import com.example.lkjhgf.trip.singleTrip.StartView_Form;

import java.util.ArrayList;

public class TripComplete extends MyTrip {

    public TripComplete(Activity activity, View view, ArrayList<TripItem> tripItems) {
        super(activity, view, tripItems);

        abort.setVisibility(View.GONE);
        calculateTickets.setVisibility(View.GONE);

        setOnClickListener();


        recyclerViewBlah();
    }

    private void setOnClickListener(){
        addTrip.setOnClickListener(v -> {
            Intent newIntent = new Intent(activity, StartView_Form.class);
            super.startNextActivity(newIntent);
        });
    }



}
