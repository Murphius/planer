package com.example.lkjhgf.helper.futureTrip;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.lkjhgf.futureTrips.recyclerView.TripItem;
import com.example.lkjhgf.helper.closeUp.MultipleCloseUp;
import com.example.lkjhgf.trip.multipleTrips.StartView_Form;

import java.util.ArrayList;

public class TripIncomplete extends MyTrip {

    public static String EXTRA_NUM_TRIP = "com.example.lkjhgf.helper.futureTrip.EXTRA_NUM_TRIP";

    private int numTrip;

    public TripIncomplete(Activity activity, View view, ArrayList<TripItem> tripItems) {
        super(activity, view, tripItems);

        numTrip = activity.getIntent().getIntExtra(MultipleCloseUp.EXTRA_NUM_TRIP, 1);

        setOnClickListener();
        recyclerViewBlah();
    }

    private void setOnClickListener() {
        addTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(activity, StartView_Form.class);
                newIntent.putExtra(EXTRA_NUM_TRIP, numTrip + 1);
                startNextActivity(newIntent);
            }
        });
    }
}
