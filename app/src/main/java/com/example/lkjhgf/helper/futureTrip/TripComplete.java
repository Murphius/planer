package com.example.lkjhgf.helper.futureTrip;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.lkjhgf.futureTrips.recyclerView.TripItem;
import com.example.lkjhgf.trip.multipleTrips.EditMultipleTrip;
import com.example.lkjhgf.trip.singleTrip.CopySingleTrip;
import com.example.lkjhgf.trip.singleTrip.EditSingleTrip;
import com.example.lkjhgf.trip.singleTrip.StartView_Form;

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
            Intent newIntent = new Intent(activity, StartView_Form.class);
            super.startNextActivity(newIntent);
        });


    }
}
