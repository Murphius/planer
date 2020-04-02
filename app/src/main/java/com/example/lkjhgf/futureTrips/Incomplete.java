package com.example.lkjhgf.futureTrips;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.lkjhgf.R;
import com.example.lkjhgf.futureTrips.recyclerView.TripItem;
import com.example.lkjhgf.helper.futureTrip.TripIncomplete;

import java.util.ArrayList;

import de.schildbach.pte.dto.Trip;

import static com.example.lkjhgf.helper.closeUp.CloseUp.EXTRA_TRIP;
import static com.example.lkjhgf.helper.closeUp.MultipleCloseUp.EXTRA_NUM_ADULT;
import static com.example.lkjhgf.helper.closeUp.MultipleCloseUp.EXTRA_NUM_CHILDREN;

public class Incomplete extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.future_trips);
        View view = findViewById(R.id.constraintLayout3);

        Intent intent = getIntent();
        Trip trip = (Trip) intent.getSerializableExtra(EXTRA_TRIP);
        int numAdult = intent.getIntExtra(EXTRA_NUM_ADULT,0);
        int numChildren = intent.getIntExtra(EXTRA_NUM_CHILDREN, 0);
        ArrayList<TripItem> tripItems = new ArrayList<>();
        TripItem newTripItem = new TripItem(trip, false, numAdult, numChildren);
        if(!tripItems.contains(newTripItem)){
            tripItems.add(newTripItem);
        }

        new TripIncomplete(this, view, tripItems);
    }

}
