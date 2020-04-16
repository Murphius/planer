package com.example.lkjhgf.helper.futureTrip;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.lkjhgf.recyclerView.futureTrips.TripItem;
import com.example.lkjhgf.activites.singleTrip.UserForm;

public class TripComplete extends MyTrip {

    public TripComplete(Activity activity, View view, TripItem tripItem) {
        super(activity, view, tripItem, MyTrip.ALL_SAVED_TRIPS);

        abort.setVisibility(View.GONE);
        calculateTickets.setVisibility(View.GONE);

        setOnClickListener();
        recyclerViewBlah();
    }


    private void setOnClickListener() {
        addTrip.setOnClickListener(v -> {
            Intent newIntent = new Intent(activity, UserForm.class);
            super.startNextActivity(newIntent);
        });


    }
}
