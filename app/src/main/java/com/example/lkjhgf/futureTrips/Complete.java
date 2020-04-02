package com.example.lkjhgf.futureTrips;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.lkjhgf.R;
import com.example.lkjhgf.futureTrips.recyclerView.TripItem;
import com.example.lkjhgf.helper.futureTrip.TripComplete;

import java.util.ArrayList;

import de.schildbach.pte.dto.Trip;

import static com.example.lkjhgf.helper.closeUp.CloseUp.EXTRA_TRIP;

public class Complete extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.future_trips);

        View view = findViewById(R.id.constraintLayout3);

        Intent intent = getIntent();
        Trip trip = (Trip) intent.getSerializableExtra(EXTRA_TRIP);

        ArrayList<TripItem> tripItems = new ArrayList<>();

        TripItem newTripItem = new TripItem(trip, true);

        if(!tripItems.contains(newTripItem)){
            tripItems.add(newTripItem);
        }

        //TODO ? farbe, trip
        new TripComplete(this, view, tripItems);
    }
}
