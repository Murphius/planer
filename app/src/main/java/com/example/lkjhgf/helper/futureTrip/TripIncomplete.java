package com.example.lkjhgf.helper.futureTrip;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.lkjhgf.activites.futureTrips.Complete;
import com.example.lkjhgf.futureTrips.recyclerView.TripItem;
import com.example.lkjhgf.helper.closeUp.MultipleCloseUp;
import com.example.lkjhgf.activites.multipleTrips.CopyTrip;
import com.example.lkjhgf.activites.multipleTrips.UserForm;

import java.util.ArrayList;

public class TripIncomplete extends MyTrip {

    private int numTrip;

    public TripIncomplete(Activity activity, View view, TripItem tripItem) {
        super(activity, view, tripItem, MyTrip.SAVED_TRIPS);

        numTrip = activity.getIntent().getIntExtra(MultipleCloseUp.EXTRA_NUM_TRIP, 1);

        setOnClickListener();
        recyclerViewBlah();
    }

    void myOnCopyClicked(int position) {
        //loadData();
        Intent newIntent = new Intent(activity.getApplicationContext(), CopyTrip.class);
        TripItem tripItem = tripItems.get(position);
        newIntent.putExtra(EXTRA_NUM_TRIP, numTrip);
        newIntent.putExtra(EXTRA_NUM_ADULT, tripItem.getNumAdult());
        newIntent.putExtra(EXTRA_NUM_CHILDREN, tripItem.getNumChildren());
        newIntent.putExtra(EXTRA_TRIP, tripItem.getTrip());
        setIntent(newIntent,position);
        startNextActivity(newIntent);
    }

    private void setOnClickListener() {
        addTrip.setOnClickListener(v -> {
            Intent newIntent = new Intent(activity, UserForm.class);
            newIntent.putExtra(EXTRA_NUM_TRIP, numTrip + 1);
            startNextActivity(newIntent);
        });

        abort.setOnClickListener(v -> activity.onBackPressed());

        //TODO Kalkulierung der Tickets
        calculateTickets.setOnClickListener(v -> {
            ArrayList<TripItem> copy = new ArrayList<>();
           copy.addAll(tripItems);
            dataPath = ALL_SAVED_TRIPS;
            tripItems.clear();
            loadData();
            for(TripItem item : copy){
                insertTrip(item);
            }
            //TODO eigentlich anzeigen der Tickets statt aller Fahrten
            Intent newIntent = new Intent(activity.getApplicationContext(), Complete.class);
            startNextActivity(newIntent);
        });
    }

    @Override
    void removeItemAtPosition(int position){
        numTrip -= 1;
        super.removeItemAtPosition(position);
    }

    private void setIntent(Intent newIntent, int position){

    }
}
